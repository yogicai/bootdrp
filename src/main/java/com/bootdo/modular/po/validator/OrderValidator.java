package com.bootdo.modular.po.validator;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.po.controller.request.OrderVO;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yogiCai
 * @date 2018-02-04 10:07:05
 */
@Service
public class OrderValidator {
    @Resource
    private OrderDao orderDao;
    @Resource
    private RPOrderDao rpOrderDao;

    public void validateSave(OrderVO order) {
        if (order.getBillDate() == null || order.getVendorId() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        if (CollectionUtils.isEmpty(MapUtil.get(params, "billNos", List.class))
                || !EnumCollection.AUDIT_STATUS.contains(AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus")))) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        if (AuditStatus.NO.equals(auditStatus)) {
            Set<String> billNoSet = Sets.newHashSet();
            List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("srcBillNos", MapUtil.get(params, "billNos", List.class)));
            for (RPOrderDO rpOrderDO : rpOrderDOList) {
                if (AuditStatus.YES.equals(rpOrderDO.getAuditStatus())) {
                    billNoSet.add(rpOrderDO.getBillNo());
                }
            }
            if (!billNoSet.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_AUDIT, "付款单", JSONUtil.toJsonStr(billNoSet)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) {
            return;
        }
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", billNos));
        for (OrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.equals(orderDO.getAuditStatus())) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
        List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("billNos", billNos));
        if (!CollectionUtils.isEmpty(rpOrderDOList)) {
            Set<String> billNoSet = Sets.newHashSet();
            for (RPOrderDO rpOrderDO : rpOrderDOList) {
                billNoSet.add(rpOrderDO.getBillNo());
            }
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_REMOVE, "付款单", JSONUtil.toJsonStr(billNoSet)));
        }
    }
}