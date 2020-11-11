package com.bootdo.report.enums;

/**
 * @author caiyz
 * @since 2020-11-10 11:00
 */
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
    COUNT("count", "订单数");


    private final String value;
    private final String text;

    public String column() {
        return value;
    }

    public String text() {
        return text;
    }

    BillStatType(String column, String text) {
        this.value = column;
        this.text = text;
    }

}