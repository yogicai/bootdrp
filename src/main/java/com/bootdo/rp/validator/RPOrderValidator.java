package com.bootdo.rp.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.EnumCollection;
import com.bootdo.common.exception.biz.assertion.BizServiceException;
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
public class RPOrderValidator {
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private SEOrderDao seOrderDao;

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
        List<RPOrderDO> orderDOList = rpOrderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        List<String> billNos = MapUtil.get(params, "billNos", List.class);
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        if (CollectionUtils.isEmpty(billNos) || !EnumCollection.AUDIT_STATUS.contains(auditStatus)) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        //审核需要判断收付款单对应的采购销售单是否是审核状态
        if (AuditStatus.YES.equals(auditStatus)) {
            List<RPOrderEntryDO> orderEntryDOList = rpOrderEntryDao.list(ImmutableMap.of("billNos", billNos));
            Set<String> srcBillNoSet = Sets.newHashSet();
            for (RPOrderEntryDO entryDO : orderEntryDOList) {
                srcBillNoSet.add(entryDO.getSrcBillNo());
            }
            if (CollectionUtils.isEmpty(srcBillNoSet)) {
                return;
            }

            //收款单对应的校验销售单是否已经审核了
            List<SEOrderDO> seOrderDOList = seOrderDao.list(ImmutableMap.of("billNos", srcBillNoSet));
            Set<String> auditBillNoSet = Sets.newHashSet();
            for (SEOrderDO orderDO : seOrderDOList) {
                if (AuditStatus.NO.equals(orderDO.getAuditStatus())) {
                    auditBillNoSet.add(orderDO.getBillNo());
                }
            }
            if (!auditBillNoSet.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "销售单", JSONUtil.toJsonStr(auditBillNoSet)));
            }
            //付款单对应的校验采购单是否已经审核了
            List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", srcBillNoSet));
            Set<String> auditBillNoSet1 = Sets.newHashSet();
            for (OrderDO orderDO : orderDOList) {
                if (AuditStatus.NO.equals(orderDO.getAuditStatus())) {
                    auditBillNoSet1.add(orderDO.getBillNo());
                }
            }
            if (!auditBillNoSet1.isEmpty()) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.RP_ORDER_AUDIT, "采购单", JSONUtil.toJsonStr(auditBillNoSet1)));
            }
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) {
            return;
        }
        List<RPOrderDO> orderDOList = rpOrderDao.list(ImmutableMap.of("billNos", billNos));
        for (RPOrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.equals(orderDO.getAuditStatus())) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
    }

}