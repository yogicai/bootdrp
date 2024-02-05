package com.bootdo.modular.se.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.param.SEOrderVO;
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
public class SEOrderValidator {
    @Resource
    private SEOrderService seOrderService;
    @Resource
    private RPOrderService rpOrderService;

    public void validateSave(SEOrderVO order) {
        if (order.getBillDate() == null || order.getConsumerId() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<SEOrderDO> orderDOList = seOrderService.list(Wrappers.lambdaQuery(SEOrderDO.class).eq(SEOrderDO::getBillNo, order.getBillNo()));
        if (CollUtil.isNotEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(OrderAuditParam param) {
        if (CollUtil.isEmpty(param.getBillNos()) || !EnumCollection.AUDIT_STATUS.contains(param.getAuditStatus())) {
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
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_AUDIT, "收款单", JSONUtil.toJsonStr((billNoSet))));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollUtil.isEmpty(billNos)) {
            return;
        }
        List<SEOrderDO> orderDOList = seOrderService.list(Wrappers.lambdaQuery(SEOrderDO.class).in(SEOrderDO::getBillNo, billNos));
        if (orderDOList.stream().anyMatch(orderDO -> AuditStatus.YES.equals(orderDO.getAuditStatus()))) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
        }

        List<RPOrderDO> rpOrderDOList = rpOrderService.list(Wrappers.lambdaQuery(RPOrderDO.class).in(RPOrderDO::getBillNo, billNos));
        if (CollUtil.isNotEmpty(rpOrderDOList)) {
            Set<String> billNoSet = rpOrderDOList.stream().map(RPOrderDO::getBillNo).collect(Collectors.toSet());
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_REMOVE, "收款单", JSONUtil.toJsonStr(billNoSet)));
        }
    }

}