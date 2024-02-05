package com.bootdo.modular.rp.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.po.service.OrderService;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.param.RPOrderVO;
import com.bootdo.modular.rp.service.RPOrderEntryService;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.service.SEOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yogiCai
 * @since 2018-02-04 10:07:05
 */
@Service
public class RPOrderValidator {
    @Resource
    private RPOrderService rpOrderService;
    @Resource
    private RPOrderEntryService rpOrderEntryService;
    @Resource
    private OrderService orderService;
    @Resource
    private SEOrderService seOrderService;


    public void validateSave(RPOrderVO order) {
        if (order.getBillDate() == null || order.getDebtorId() == null || order.getCheckId() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (CollUtil.isEmpty(order.getEntryVOList()) || order.getEntryVOList().stream().anyMatch(entry -> ObjectUtil.isNull(entry.getCheckAmount()))) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.RP_ORDER_CHECK_ORDER_NULL);
        }
        if (CollUtil.isEmpty(order.getSettleVOList()) || order.getSettleVOList().stream().anyMatch(entry -> ObjectUtil.isNull(entry.getPaymentAmount()))) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.RP_ORDER_SETTLE_ITEM_NULL);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<RPOrderDO> orderDOList = rpOrderService.list(Wrappers.lambdaQuery(RPOrderDO.class).eq(RPOrderDO::getBillNo, order.getBillNo()));
        if (CollUtil.isNotEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(OrderAuditParam param) {
        List<String> billNos = param.getBillNos();
        AuditStatus auditStatus = param.getAuditStatus();
        if (CollUtil.isEmpty(billNos) || !EnumCollection.AUDIT_STATUS.contains(auditStatus)) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        //审核需要判断收付款单对应的采购销售单是否是审核状态
        if (AuditStatus.YES.equals(auditStatus)) {
            List<RPOrderEntryDO> orderEntryDOList = rpOrderEntryService.list(Wrappers.lambdaQuery(RPOrderEntryDO.class).in(RPOrderEntryDO::getBillNo, billNos));
            Set<String> srcBillNoSet = orderEntryDOList.stream().map(RPOrderEntryDO::getSrcBillNo).collect(Collectors.toSet());
            if (CollUtil.isEmpty(srcBillNoSet)) {
                return;
            }
            //收款单对应的校验销售单是否已经审核了
            List<SEOrderDO> seOrderDOList = seOrderService.list(Wrappers.lambdaQuery(SEOrderDO.class).in(SEOrderDO::getBillNo, srcBillNoSet));
            Set<String> auditBillNoSet = seOrderDOList.stream().filter(orderDO -> AuditStatus.NO.equals(orderDO.getAuditStatus())).map(SEOrderDO::getBillNo).collect(Collectors.toSet());
            if (!auditBillNoSet.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "销售单", JSONUtil.toJsonStr(auditBillNoSet)));
            }
            //付款单对应的校验采购单是否已经审核了
            List<OrderDO> orderDOList = orderService.list(Wrappers.lambdaQuery(OrderDO.class).in(OrderDO::getBillNo, srcBillNoSet));
            Set<String> auditBillNoSet1 = orderDOList.stream().filter(orderDO -> AuditStatus.NO.equals(orderDO.getAuditStatus())).map(OrderDO::getBillNo).collect(Collectors.toSet());
            if (!auditBillNoSet1.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "采购单", JSONUtil.toJsonStr(auditBillNoSet1)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollUtil.isEmpty(billNos)) {
            return;
        }
        List<RPOrderDO> orderDOList = rpOrderService.list(Wrappers.lambdaQuery(RPOrderDO.class).in(RPOrderDO::getBillNo, billNos));
        for (RPOrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.equals(orderDO.getAuditStatus())) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
    }

}