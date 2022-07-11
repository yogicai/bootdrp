package com.bootdo.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum OrderStatus implements EnumBean {

    /** 未结款  */
    WAITING_PAY("未结款"),
    PART_PAY("部分结款"),
    FINISH_PAY("全部结款"),
    ORDER_CANCEL("订单取消");

    private final String remark;

}
