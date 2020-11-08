package com.bootdo.rp.validator;

import com.bootdo.common.constants.ErrorMessage;
import com.bootdo.common.constants.OrderStatusCode;
import com.bootdo.common.exception.BusinessException;
import com.bootdo.rp.domain.PointEntryDO;
import org.springframework.stereotype.Service;

/**
 * @Author: yogiCai
 * @Date: 2018-02-04 10:07:05
 */
@Service
public class RPPointValidator {

    public void validateSave(PointEntryDO pointEntry) {
        if (pointEntry.getConsumerId() == null || pointEntry.getSource() == null ) {
            throw new BusinessException(OrderStatusCode.ORDER_INVALID, ErrorMessage.PARAM_INVALID);
        }

    }

}