package com.bootdo.modular.po.service;

import cn.hutool.core.collection.CollUtil;
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
import com.bootdo.modular.po.convert.OrderConverter;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.dao.OrderEntryDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.domain.OrderEntryDO;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.po.param.OrderQryParam;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.rp.service.RPOrderEntryService;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.rp.service.RPOrderSettleService;
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
public class OrderService extends ServiceImpl<OrderDao, OrderDO> {
    @Resource
    private AccountDao accountDao;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private RPOrderService rpOrderService;
    @Resource
    private RPOrderEntryService rpOrderEntryService;
    @Resource
    private RPOrderSettleService rpOrderSettleService;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public PageJQ page(OrderQryParam param) {
        return new PageJQ(this.pageList(PageFactory.defaultPage(), param));
    }

    public Page<OrderDO> pageList(Page<OrderDO> page, OrderQryParam param) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .in(ObjectUtil.isNotEmpty(param.getVendorId()), OrderDO::getVendorId, StrUtil.split(param.getVendorId(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getBillType()), OrderDO::getBillType, StrUtil.split(param.getBillType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getAuditStatus()), OrderDO::getAuditStatus, StrUtil.split(param.getAuditStatus(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), OrderDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .notIn(ObjectUtil.isNotEmpty(param.getStatusNot()), OrderDO::getStatus, param.getStatusNot())
                .ge(ObjectUtil.isNotEmpty(param.getStart()), OrderDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), OrderDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(OrderDO::getBillNo, param.getSearchText()).or().like(OrderDO::getVendorName, param.getSearchText()).or().like(OrderDO::getRemark, param.getSearchText()))
                .orderByDesc(OrderDO::getBillDate).orderByDesc(OrderDO::getUpdateTime);

        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(OrderAuditParam param) {
        AuditStatus auditStatus = param.getAuditStatus();
        List<OrderDO> orderDOList = this.list(Wrappers.lambdaQuery(OrderDO.class).in(OrderDO::getBillNo, param.getBillNos()));
        //去除已经是审核（未审核）状态的订单
        List<OrderDO> auditOrderList = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //审核采购单重新计算商品成本、生成收付款单
        auditOrderList.forEach(orderDO -> {
            handleCostAndAudit(orderDO, auditStatus);
            handleRpOrder(orderDO, auditStatus);
        });
    }

    /**
     * 重新计算商品成本及审核订单
     */
    private void handleCostAndAudit(OrderDO orderDO, AuditStatus auditStatus) {
        costAmountCalculator.calcPOBillCost(orderDO, auditStatus);
        this.update(Wrappers.lambdaUpdate(OrderDO.class)
                .set(OrderDO::getAuditStatus, auditStatus)
                .eq(OrderDO::getBillNo, orderDO.getBillNo())
                .ne(OrderDO::getAuditStatus, auditStatus));
    }

    /**
     * 付款单入库
     */
    private void handleRpOrder(OrderDO orderDO, AuditStatus auditStatus) {
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
        RPOrderDO rpOrderDO = OrderConverter.convertRPOrder(orderDO);
        List<RPOrderEntryDO> rpOrderEntryDOList = OrderConverter.convertRPOrderEntry(rpOrderDO, orderDO);
        List<RPOrderSettleDO> rpOrderSettleDOList = OrderConverter.convertRPOrderSettle(rpOrderDO, orderDO, accountDO);
        //保存付款单
        rpOrderService.save(rpOrderDO);
        rpOrderEntryService.saveBatch(rpOrderEntryDOList);
        rpOrderSettleService.saveBatch(rpOrderSettleDOList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<String> billNos) {
        this.remove(Wrappers.lambdaQuery(OrderDO.class).in(OrderDO::getBillNo, billNos));
        orderEntryDao.delete(Wrappers.lambdaQuery(OrderEntryDO.class).in(OrderEntryDO::getBillNo, billNos));
    }

}
