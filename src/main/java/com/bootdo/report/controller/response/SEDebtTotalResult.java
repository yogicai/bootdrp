package com.bootdo.report.controller.response;

import lombok.Data;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class SEDebtTotalResult {
    /** 客户欠款 */
    private BigDecimal debtAmount = BigDecimal.ZERO;
    /** 供应商欠款 */
    private BigDecimal debtVAmount = BigDecimal.ZERO;

}
