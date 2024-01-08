package com.bootdo.modular.wh.validator;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.wh.dao.WHOrderDao;
import com.bootdo.modular.wh.param.WHOrderVO;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @Date: 2018-02-04 10:07:05
 */
@Service
public class WHOrderValidator {
    @Resource
    private WHOrderDao whOrderDao;

    public void validateSave(WHOrderVO order) {
        if (order.getBillDate() == null || order.getServiceType() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<WHOrderDO> orderDOList = whOrderDao.list(ImmutableMap.of("billNo", order.getBillNo()));
        if (!CollectionUtils.isEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(Map<String, Object> params) {
        if (CollectionUtils.isEmpty(MapUtil.get(params, "billNos", List.class))
                || !EnumCollection.AUDIT_STATUS.contains(AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus")))) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) {
            return;
        }
        List<WHOrderDO> orderDOList = whOrderDao.list(ImmutableMap.of("billNos", billNos));
        for (WHOrderDO orderDO : orderDOList) {
            if (AuditStatus.YES.equals(orderDO.getAuditStatus())) {
                throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
            }
        }
    }

}