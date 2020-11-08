package com.bootdo.rp.validator;

import com.alibaba.fastjson.JSON;
import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.EnumCollection;
import com.bootdo.common.exception.BusinessException;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.po.dao.OrderDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.rp.controller.request.RPOrderVO;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.dao.RPOrderEntryDao;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.se.dao.SEOrderDao;
import com.bootdo.se.domain.SEOrderDO;
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
public class RPOrderValidator {
    @Autowired
    private RPOrderDao rpOrderDao;
    @Autowired
    private RPOrderEntryDao rpOrderEntryDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SEOrderDao seOrderDao;

    public void validateSave(RPOrderVO order) {
        if (order.getBillDate() == null || order.getDebtorId() == null || order.getCheckId() == null) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StringUtil.isEmpty(order.getBillNo())) return;
        List<RPOrderDO> orderDOList = rpOrderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.name().equals(orderDOList.get(0).getAuditStatus())) {
            throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        List<String> billNos = MapUtils.getList(params, "billNos");
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtils.getString(params, "auditStatus"));
        if (CollectionUtils.isEmpty(billNos) || !EnumCollection.AUDIT_STATUS.contains(auditStatus)) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        //审核需要判断收付款单对应的采购销售单是否是审核状态
        if (AuditStatus.YES.equals(auditStatus)) {
            List<RPOrderEntryDO> orderEntryDOList = rpOrderEntryDao.list(ImmutableMap.of("billNos", billNos));
            Set<String> srcBillNoSet = Sets.newHashSet();
            for (RPOrderEntryDO entryDO : orderEntryDOList) {
                srcBillNoSet.add(entryDO.getSrcBillNo());
            }
            if (CollectionUtils.isEmpty(srcBillNoSet)) return;

            //收款单对应的校验销售单是否已经审核了
            List<SEOrderDO> seOrderDOList = seOrderDao.list(ImmutableMap.of("billNos", srcBillNoSet));
            Set<String> auditBillNoSet = Sets.newHashSet();
            for (SEOrderDO orderDO : seOrderDOList) {
                if (AuditStatus.NO.name().equals(orderDO.getAuditStatus())) {
                    auditBillNoSet.add(orderDO.getBillNo());
                }
            }
            if (auditBillNoSet.size() > 0) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "销售单", JSON.toJSONString(auditBillNoSet)));
            }
            //付款单对应的校验采购单是否已经审核了
            List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", srcBillNoSet));
            Set<String> auditBillNoSet1 = Sets.newHashSet();
            for (OrderDO orderDO : orderDOList) {
                if (AuditStatus.NO.name().equals(orderDO.getAuditStatus())) {
                    auditBillNoSet1.add(orderDO.getBillNo());
                }
            }
            if (auditBillNoSet.size() > 0) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "采购单", JSON.toJSONString(auditBillNoSet1)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) return;
        List<RPOrderDO> orderDOList = rpOrderDao.list(ImmutableMap.of("billNos", billNos));
        for (RPOrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.name().equals(orderDO.getAuditStatus())) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
    }

}