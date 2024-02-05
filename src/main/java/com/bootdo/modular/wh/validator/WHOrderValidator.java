package com.bootdo.modular.wh.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.EnumCollection;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.param.WHOrderVO;
import com.bootdo.modular.wh.service.WHOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-04 10:07:05
 */
@Service
public class WHOrderValidator {
    @Resource
    private WHOrderService whOrderService;

    public void validateSave(WHOrderVO order) {
        if (order.getBillDate() == null || order.getServiceType() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
        if (StrUtil.isEmpty(order.getBillNo())) {
            return;
        }
        List<WHOrderDO> orderDOList = whOrderService.list(Wrappers.lambdaQuery(WHOrderDO.class).eq(WHOrderDO::getBillNo, order.getBillNo()));
        if (CollUtil.isNotEmpty(orderDOList) && AuditStatus.YES.equals(orderDOList.get(0).getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "修改"));
        }
    }

    public void validateAudit(OrderAuditParam param) {
        if (CollUtil.isEmpty(param.getBillNos()) || !EnumCollection.AUDIT_STATUS.contains(param.getAuditStatus())) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }
    }

    public void validateRemove(List<String> billNos) {
        if (CollectionUtils.isEmpty(billNos)) {
            return;
        }
        List<WHOrderDO> orderDOList = whOrderService.list(Wrappers.lambdaQuery(WHOrderDO.class).in(WHOrderDO::getBillNo, billNos));
        if (orderDOList.stream().anyMatch(orderDO -> AuditStatus.YES.equals(orderDO.getAuditStatus()))) {
            throw new BizServiceException(OrderStatusCode.ORDER_PROCESS, String.format(ErrorMessage.STATUS_AUDIT_YES, "删除"));
        }
    }

}