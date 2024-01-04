package com.bootdo.modular.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caiyz
 * @since 2020-11-10 11:00
 */
@Getter
@AllArgsConstructor
public enum BillStatType {

    /** 营业额 */
    SALE("totalAmount", "营业额"),
    /** 成本 */
    COST("costAmount", "成本"),
    /** 毛利润 */
    PROFIT("profitAmount", "毛利润"),
    /** 已付款 */
    PAYMENT("paymentAmount", "已付款"),
    /** 欠款 */
    DEBT("debtAmount", "欠款"),
    /** 订单数 */
    COUNT("count", "订单数"),

    ;


    private final String value;
    private final String text;

}