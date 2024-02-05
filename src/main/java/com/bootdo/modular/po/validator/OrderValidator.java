package com.bootdo.modular.po.validator;

import cn.hutool.core.collection.CollUtil;
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
import com.bootdo.modular.po.param.OrderVO;
import com.bootdo.modular.po.service.OrderService;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.rp.service.RPOrderService;
import org.apache.commons.collections.CollectionUtils;
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
public class OrderValidator {
    @Resource
    private OrderService orderService;
    @Resource
    private RPOrderService rpOrderService;

    public void validateSave(OrderVO order) {
        if (order.getBillDate() == null || order.getVendorId() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<OrderDO> orderDOList = orderService.list(Wrappers.lambdaQuery(OrderDO.class).eq(OrderDO::getBillNo, order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(OrderAuditParam param) {
        if (CollectionUtils.isEmpty(param.getBillNos()) || !EnumCollection.AUDIT_STATUS.contains(param.getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        AuditStatus auditStatus = param.getAuditStatus();
        if (AuditStatus.NO.equals(auditStatus)) {

            Set<String> billNoSet = rpOrderService.selectJoinList(RPOrderQryParam.builder()
                            .srcBillNo(StrUtil.join(StrUtil.COMMA, param.getBillNos())).build())
                    .stream()
                    .filter(rpOrderDO -> AuditStatus.YES.equals(rpOrderDO.getAuditStatus()))
                    .map(RPOrderDO::getBillNo)
                    .collect(Collectors.toSet());

            if (!billNoSet.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_AUDIT, "付款单", JSONUtil.toJsonStr(billNoSet)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollUtil.isEmpty(billNos)) {
            return;
        }
        List<OrderDO> orderDOList = orderService.list(Wrappers.lambdaQuery(OrderDO.class).in(OrderDO::getBillNo, billNos));
        if (orderDOList.stream().anyMatch(orderDO -> AuditStatus.YES.equals(orderDO.getAuditStatus()))) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
        }

        List<RPOrderDO> rpOrderDOList = rpOrderService.list(Wrappers.lambdaQuery(RPOrderDO.class).in(RPOrderDO::getBillNo, billNos));
        if (CollUtil.isNotEmpty(rpOrderDOList)) {
            Set<String> billNoSet = rpOrderDOList.stream().map(RPOrderDO::getBillNo).collect(Collectors.toSet());
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_REMOVE, "付款单", JSONUtil.toJsonStr(billNoSet)));
        }
    }

}