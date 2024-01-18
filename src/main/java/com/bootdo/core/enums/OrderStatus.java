package com.bootdo.core.enums;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum OrderStatus implements EnumBean<OrderStatus> {

    /**
     * 未结款
     */
    WAITING_PAY("未结款"),
    PART_PAY("部分结款"),
    FINISH_PAY("全部结款"),
    ORDER_CANCEL("订单取消");

    private final String remark;


    public static OrderStatus fromPayment(BigDecimal paymentAmount, BigDecimal totalAmount) {

        if (NumberUtil.null2Zero(paymentAmount).compareTo(BigDecimal.ZERO) == 0) {
            return WAITING_PAY;
        }
        if (NumberUtil.sub(totalAmount, paymentAmount).compareTo(BigDecimal.ZERO) <= 0) {
            return FINISH_PAY;
        }
        return PART_PAY;
    }

}
