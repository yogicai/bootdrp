package com.bootdo.modular.rp.validator;

import com.bootdo.core.consts.ErrorMessage;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.exception.assertion.BizServiceException;
import com.bootdo.modular.rp.domain.PointEntryDO;
import org.springframework.stereotype.Service;

/**
 * @author yogiCai
 * @date 2018-02-04 10:07:05
 */
@Service
public class RPPointValidator {

    public void validateSave(PointEntryDO pointEntry) {
        if (pointEntry.getConsumerId() == null || pointEntry.getSource() == null) {
            throw new BizServiceException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }

    }

}