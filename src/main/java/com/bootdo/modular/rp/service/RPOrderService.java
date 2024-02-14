package com.bootdo.modular.rp.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.dao.RPOrderSettleDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class RPOrderService extends ServiceImpl<RPOrderDao, RPOrderDO> {
    @Resource
    private OrderDao orderDao;
    @Resource
    private SEOrderDao seOrderDao;
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private RPOrderSettleDao rpOrderSettleDao;

    private static final EnumSet<BillType> PO_BILL_SET = EnumSet.of(BillType.CG_ORDER, BillType.TH_ORDER);
    private static final EnumSet<BillType> SE_BILL_SET = EnumSet.of(BillType.XS_ORDER);


    public PageJQ page(RPOrderQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<RPOrderDO> pageList(Page<RPOrderDO> page, RPOrderQryParam param) {
        LambdaQueryWrapper<RPOrderDO> queryWrapper = Wrappers.lambdaQuery(RPOrderDO.class)
                .in(ObjectUtil.isNotEmpty(param.getBillType()), RPOrderDO::getBillType, StrUtil.split(param.getBillType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getAuditStatus()), RPOrderDO::getAuditStatus, StrUtil.split(param.getAuditStatus(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getCheckId()), RPOrderDO::getCheckId, StrUtil.split(param.getCheckId(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), RPOrderDO::getBillDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), RPOrderDO::getBillDate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(RPOrderDO::getBillNo, param.getSearchText()).or().like(RPOrderDO::getRemark, param.getSearchText()))
                .orderByDesc(RPOrderDO::getBillDate).orderByDesc(RPOrderDO::getUpdateTime);

        return this.page(page, queryWrapper);
    }

    public List<RPOrderDO> list(RPOrderQryParam param) {
        List<RPOrderDO> rpOrderDOList = this.pageList(PageFactory.defalultAllPage(), param).getRecords();
        Set<String> billNoSet = rpOrderDOList.stream().map(RPOrderDO::getBillNo).collect(Collectors.toSet());

        Map<String, List<RPOrderEntryDO>> entryListMap = rpOrderEntryDao.selectList(Wrappers.lambdaQuery(RPOrderEntryDO.class).in(RPOrderEntryDO::getBillNo, billNoSet))
                .stream().collect(Collectors.groupingBy(RPOrderEntryDO::getBillNo, Collectors.toList()));

        Map<String, List<RPOrderSettleDO>> settleListMap = rpOrderSettleDao.selectList(Wrappers.lambdaQuery(RPOrderSettleDO.class).in(RPOrderSettleDO::getBillNo, billNoSet))
                .stream().collect(Collectors.groupingBy(RPOrderSettleDO::getBillNo, Collectors.toList()));

        rpOrderDOList.forEach(rpOrderDO -> {
            rpOrderDO.getEntryDOList().addAll(entryListMap.getOrDefault(rpOrderDO.getBillNo(), Collections.emptyList()));
            rpOrderDO.getSettleDOList().addAll(settleListMap.getOrDefault(rpOrderDO.getBillNo(), Collections.emptyList()));
        });

        return rpOrderDOList;
    }

    public PageJQ selectJoinPage(RPOrderQryParam param) {
        return new PageJQ(this.baseMapper.selectJoinPage(PageFactory.defaultPage(), RPOrderDO.class, selectJoinWrapper(param)));
    }

    public List<RPOrderDO> selectJoinList(RPOrderQryParam param) {
        return this.baseMapper.selectJoinList(RPOrderDO.class, selectJoinWrapper(param));
    }

    public Long selectJoinCount(RPOrderQryParam param) {
        return this.baseMapper.selectJoinCount(selectJoinWrapper(param));
    }

    private MPJLambdaWrapper<RPOrderDO> selectJoinWrapper(RPOrderQryParam param) {
        return JoinWrappers.lambda(RPOrderDO.class)
                .selectCollection(RPOrderEntryDO.class, RPOrderDO::getEntryDOList)
                .selectCollection(RPOrderSettleDO.class, RPOrderDO::getSettleDOList)
                .leftJoin(RPOrderEntryDO.class, RPOrderEntryDO::getBillNo, RPOrderDO::getBillNo)
                .leftJoin(RPOrderSettleDO.class, RPOrderSettleDO::getBillNo, RPOrderDO::getBillNo)
                //明细表过滤条件
                .in(ObjectUtil.isNotEmpty(param.getSrcBillNo()), RPOrderEntryDO::getSrcBillNo, StrUtil.split(param.getSrcBillNo(), StrUtil.COMMA))
                //主表过滤条件
                .in(ObjectUtil.isNotEmpty(param.getBillSource()), RPOrderDO::getBillSource, StrUtil.split(param.getBillSource(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getBillType()), RPOrderDO::getBillType, StrUtil.split(param.getBillType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getAuditStatus()), RPOrderDO::getAuditStatus, StrUtil.split(param.getAuditStatus(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getCheckId()), RPOrderDO::getCheckId, StrUtil.split(param.getCheckId(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), RPOrderDO::getBillDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), RPOrderDO::getBillDate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(RPOrderDO::getBillNo, param.getSearchText())
                        .or().like(RPOrderDO::getRemark, param.getSearchText()).or().like(RPOrderEntryDO::getSrcBillNo, param.getSearchText()))
                .orderByDesc(RPOrderDO::getBillDate).orderByDesc(RPOrderDO::getUpdateTime);
    }

    /**
     * 收付款财务单审核要更新，源订单的已付金额
     */
    @Transactional(rollbackFor = Exception.class)
    public void audit(OrderAuditParam param) {
        AuditStatus auditStatus = param.getAuditStatus();
        List<RPOrderDO> rpOrderDOList = rpOrderDao.selectList(Wrappers.lambdaQuery(RPOrderDO.class).in(RPOrderDO::getBillNo, param.getBillNos()));
        //过滤已审核或未审核的订单
        Set<String> billNoSet = rpOrderDOList.stream().filter(rpOrderDO -> !auditStatus.equals(rpOrderDO.getAuditStatus())).map(RPOrderDO::getBillNo).collect(Collectors.toSet());
        if (billNoSet.isEmpty()) {
            return;
        }

        //按源订单号归类订单分录，计算本次需更新的源订单支付金额
        List<RPOrderEntryDO> rpOrderEntryDOList = rpOrderEntryDao.selectList(Wrappers.lambdaQuery(RPOrderEntryDO.class).in(RPOrderEntryDO::getBillNo, billNoSet));
        //按源订单号归集 收付款单分录
        Map<String, List<RPOrderEntryDO>> srcEntrylistMap = rpOrderEntryDOList.stream().collect(Collectors.groupingBy(RPOrderEntryDO::getSrcBillNo, Collectors.toList()));
        //源订单MAP（采购单）
        Map<String, OrderDO> orderMap = orderDao.selectList(Wrappers.lambdaQuery(OrderDO.class).in(OrderDO::getBillNo, srcEntrylistMap.keySet()))
                .stream().collect(Collectors.toMap(OrderDO::getBillNo, Function.identity(), (o, v) -> o));
        //源订单MAP（销售单）
        Map<String, SEOrderDO> seOrderMap = seOrderDao.selectList(Wrappers.lambdaQuery(SEOrderDO.class).in(SEOrderDO::getBillNo, srcEntrylistMap.keySet()))
                .stream().collect(Collectors.toMap(SEOrderDO::getBillNo, Function.identity(), (o, v) -> o));

        //更新源订单已付金额、支付状态
        srcEntrylistMap.forEach((srcBillNo, entryList) -> {
            //核销金额
            BigDecimal checkAmount = entryList.stream().map(RPOrderEntryDO::getCheckAmount)
                    .map(amount -> NumberUtil.mul(amount, AuditStatus.YES.equals(auditStatus) ? BigDecimal.ONE : NumberUtil.toBigDecimal(-1)))
                    .reduce(BigDecimal.ZERO, NumberUtil::add);
            //源订单类型
            BillType billType = entryList.get(0).getSrcBillType();
            //采购单
            if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && PO_BILL_SET.contains(billType)) {
                BigDecimal totalAmount = orderMap.get(srcBillNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(orderMap.get(srcBillNo).getPaymentAmount(), checkAmount);
                orderDao.update(null, Wrappers.lambdaUpdate(OrderDO.class)
                        .set(OrderDO::getStatus, OrderStatus.fromPayment(paymentAmount, totalAmount))
                        .set(OrderDO::getPaymentAmount, paymentAmount)
                        .eq(OrderDO::getBillNo, srcBillNo));

            } else if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && SE_BILL_SET.contains(billType)) {
                //销售单
                BigDecimal totalAmount = seOrderMap.get(srcBillNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(seOrderMap.get(srcBillNo).getPaymentAmount(), checkAmount);
                seOrderDao.update(null, Wrappers.lambdaUpdate(SEOrderDO.class)
                        .set(SEOrderDO::getStatus, OrderStatus.fromPayment(paymentAmount, totalAmount))
                        .set(SEOrderDO::getPaymentAmount, paymentAmount)
                        .eq(SEOrderDO::getBillNo, srcBillNo));
            }
        });

        this.update(Wrappers.lambdaUpdate(RPOrderDO.class)
                .set(RPOrderDO::getAuditStatus, auditStatus)
                .in(RPOrderDO::getBillNo, billNoSet)
                .ne(RPOrderDO::getAuditStatus, auditStatus));
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<String> billNos) {
        rpOrderDao.delete(Wrappers.lambdaQuery(RPOrderDO.class).in(RPOrderDO::getBillNo, billNos));
        rpOrderEntryDao.delete(Wrappers.lambdaQuery(RPOrderEntryDO.class).in(RPOrderEntryDO::getBillNo, billNos));
        rpOrderSettleDao.delete(Wrappers.lambdaQuery(RPOrderSettleDO.class).in(RPOrderSettleDO::getBillNo, billNos));
    }

}
