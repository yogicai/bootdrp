package com.bootdo.po.validator;

import com.alibaba.fastjson.JSON;
import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.EnumCollection;
import com.bootdo.common.exception.BusinessException;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.po.controller.request.OrderVO;
import com.bootdo.po.dao.OrderDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.domain.RPOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: yogiCai
 * @Date: 2018-02-04 10:07:05
 */
@Service
public class OrderValidator {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RPOrderDao rpOrderDao;

    public void validateSave(OrderVO order) {
        if (order.getBillDate() == null || order.getVendorId() == null) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StringUtil.isEmpty(order.getBillNo())) return;
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.name().equals(orderDOList.get(0).getAuditStatus())) {
            throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        if (CollectionUtils.isEmpty(MapUtils.getList(params, "billNos"))
                || !EnumCollection.AUDIT_STATUS.contains(AuditStatus.fromValue(MapUtils.getString(params, "auditStatus")))) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtils.getString(params, "auditStatus"));
        if (AuditStatus.NO.equals(auditStatus)) {
            Set<String> billNoSet = Sets.newHashSet();
            List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("srcBillNos", MapUtils.getList(params, "billNos")));
            for (RPOrderDO rpOrderDO : rpOrderDOList) {
                if (AuditStatus.YES.name().equals(rpOrderDO.getAuditStatus())) {
                    billNoSet.add(rpOrderDO.getBillNo());
                }
            }
            if (billNoSet.size() > 0) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_AUDIT, "付款单", JSON.toJSONString(billNoSet)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) return;
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", billNos));
        for (OrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.name().equals(orderDO.getAuditStatus())) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
        List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("billNos", billNos));
        if (!CollectionUtils.isEmpty(rpOrderDOList)) {
            Set<String> billNoSet = Sets.newHashSet();
            for (RPOrderDO rpOrderDO : rpOrderDOList) {
                billNoSet.add(rpOrderDO.getBillNo());
            }
            throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.CW_ORDER_REMOVE, "付款单", JSON.toJSONString(billNoSet)));
        }
    }
}