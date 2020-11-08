package com.bootdo.report.controller.response;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class SEBillTotalResult {
    //销售总额
    private BigDecimal totalAmount = BigDecimal.ZERO;
    //毛利
    private BigDecimal profit = BigDecimal.ZERO;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
