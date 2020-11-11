package com.bootdo.report.controller.response;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class SEDebtTotalResult {
    /** 客户欠款 */
    private BigDecimal debtAmount = BigDecimal.ZERO;
    /** 供应商欠款 */
    private BigDecimal debtVAmount = BigDecimal.ZERO;

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    public BigDecimal getDebtVAmount() {
        return debtVAmount;
    }

    public void setDebtVAmount(BigDecimal debtVAmount) {
        this.debtVAmount = debtVAmount;
    }
}
