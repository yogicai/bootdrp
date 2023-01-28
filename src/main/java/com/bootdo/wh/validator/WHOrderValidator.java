package com.bootdo.wh.validator;

import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.EnumCollection;
import com.bootdo.common.exception.BusinessException;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.wh.controller.request.WHOrderVO;
import com.bootdo.wh.dao.WHOrderDao;
import com.bootdo.wh.domain.WHOrderDO;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @Date: 2018-02-04 10:07:05
 */
@Service
public class WHOrderValidator {
    @Autowired
    private WHOrderDao orderDao;

    public void validateSave(WHOrderVO order) {
        if (order.getBillDate() == null || order.getServiceType() == null) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StringUtil.isEmpty(order.getBillNo())) return;
        List<WHOrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        if (CollectionUtils.isEmpty(MapUtils.getList(params, "billNos"))
                || !EnumCollection.AUDIT_STATUS.contains(AuditStatus.fromValue(MapUtils.getString(params, "auditStatus")))) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) return;
        List<WHOrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", billNos));
        for (WHOrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.equals(orderDO.getAuditStatus())) {
                throw new BusinessException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
    }

}