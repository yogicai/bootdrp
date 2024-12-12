package com.bootdo.modular.se.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.rp.service.PointEntryService;
import com.bootdo.modular.rp.service.RPOrderEntryService;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.rp.service.RPOrderSettleService;
import com.bootdo.modular.se.convert.SEOrderConverter;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.dao.SEOrderEntryDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.domain.SEOrderEntryDO;
import com.bootdo.modular.se.param.SeOrderQryParam;
import com.bootdo.modular.system.dao.DictDao;
import com.bootdo.modular.system.domain.DictDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class SEOrderService extends ServiceImpl<SEOrderDao, SEOrderDO> {
    @Resource
    private DictDao dictDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private SEOrderEntryDao seOrderEntryDao;
    @Resource
    private RPOrderService rpOrderService;
    @Resource
    private RPOrderEntryService rpOrderEntryService;
    @Resource
    private PointEntryService pointEntryService;
    @Resource
    private RPOrderSettleService rpOrderSettleService;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public PageJQ page(SeOrderQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<SEOrderDO> pageList(Page<SEOrderDO> page, SeOrderQryParam param) {
        LambdaQueryWrapper<SEOrderDO> queryWrapper = Wrappers.lambdaQuery(SEOrderDO.class)
                .in(ObjectUtil.isNotEmpty(param.getConsumerId()), SEOrderDO::getConsumerId, StrUtil.split(param.getConsumerId(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getAuditStatus()), SEOrderDO::getAuditStatus, StrUtil.split(param.getAuditStatus(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), SEOrderDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .notIn(ObjectUtil.isNotEmpty(param.getStatusNot()), SEOrderDO::getStatus, param.getStatusNot())
                .in(ObjectUtil.isNotEmpty(param.getBillSource()), SEOrderDO::getBillSource, StrUtil.split(param.getBillSource(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), SEOrderDO::getBillDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), SEOrderDO::getBillDate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(SEOrderDO::getBillNo, param.getSearchText()).or().like(SEOrderDO::getConsumerName, param.getSearchText()).or().like(SEOrderDO::getRemark, param.getSearchText()))
                .orderBy(true, param.isAsc(), SEOrderDO::getBillDate)
                .orderByDesc(SEOrderDO::getUpdateTime);

        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(OrderAuditParam param) {
        AuditStatus auditStatus = param.getAuditStatus();
        List<SEOrderDO> orderDOList = this.list(Wrappers.lambdaQuery(SEOrderDO.class).in(SEOrderDO::getBillNo, param.getBillNos()));
        //去除已经是审核（未审核）状态的订单
        List<SEOrderDO> auditOrderList = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //计算客户积分、生成收付款单
        auditOrderList.forEach(orderDO -> {
            handleCostAndAudit(orderDO, auditStatus);
            handlePoint(orderDO, auditStatus);
            handleRpOrder(orderDO, auditStatus);
        });
    }

    /**
     * 重新计算商品成本及审核订单
     */
    private void handleCostAndAudit(SEOrderDO orderDO, AuditStatus auditStatus) {
        costAmountCalculator.calcSEBillCost(orderDO, auditStatus);
        this.update(Wrappers.lambdaUpdate(SEOrderDO.class)
                .set(SEOrderDO::getAuditStatus, auditStatus)
                .eq(SEOrderDO::getBillNo, orderDO.getBillNo())
                .ne(SEOrderDO::getAuditStatus, auditStatus));
    }

    /**
     * 客户积分
     */
    private void handlePoint(SEOrderDO orderDO, AuditStatus auditStatus) {
        DictDO dictDO = dictDao.selectOne(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getType, "point_scale"));
        BigDecimal scale = ObjectUtil.defaultIfNull(dictDO, d -> NumberUtil.toBigDecimal(d.getValue()), BigDecimal.valueOf(0.1D));

        PointEntryDO pointEntryDO = SEOrderConverter.convertPointDO(orderDO, auditStatus, scale);
        pointEntryService.save(pointEntryDO);
    }

    /**
     * 付款单入库
     */
    private void handleRpOrder(SEOrderDO orderDO, AuditStatus auditStatus) {
        //是否存在手工创建的收付款单，若存在则系统不自动处理收付款单
        if (rpOrderService.selectJoinCount(RPOrderQryParam.builder()
                .srcBillNo(orderDO.getBillNo())
                .billSource(BillSource.USER.name()).build()) > 0) {
            return;
        }
        //删除上次审核自动生成的付款单
        rpOrderService.selectJoinList(RPOrderQryParam.builder().srcBillNo(orderDO.getBillNo()).build())
                .forEach(rpOrder -> rpOrderService.batchRemove(CollUtil.newArrayList(rpOrder.getBillNo())));
        //本次付款为0或反审核采购单，不用生成付款单
        if (orderDO.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0 || AuditStatus.NO.equals(auditStatus)) {
            return;
        }
        //付款单数据处理
        AccountDO accountDO = accountDao.selectOne(Wrappers.lambdaQuery(AccountDO.class).eq(AccountDO::getNo, orderDO.getSettleAccount()));
        RPOrderDO rpOrderDO = SEOrderConverter.convertRPOrder(orderDO);
        List<RPOrderEntryDO> rpOrderEntryDOList = SEOrderConverter.convertRPOrderEntry(rpOrderDO, orderDO);
        List<RPOrderSettleDO> rpOrderSettleDOList = SEOrderConverter.convertRPOrderSettle(rpOrderDO, orderDO, accountDO);
        //保存付款单
        rpOrderService.save(rpOrderDO);
        rpOrderEntryService.saveBatch(rpOrderEntryDOList);
        rpOrderSettleService.saveBatch(rpOrderSettleDOList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<String> billNos) {
        this.remove(Wrappers.lambdaQuery(SEOrderDO.class).in(SEOrderDO::getBillNo, billNos));
        seOrderEntryDao.delete(Wrappers.lambdaQuery(SEOrderEntryDO.class).in(SEOrderEntryDO::getBillNo, billNos));
    }

}
