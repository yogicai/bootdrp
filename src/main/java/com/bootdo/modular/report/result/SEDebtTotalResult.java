package com.bootdo.modular.report.result;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SEDebtTotalResult {
    /**
     * 客户欠款
     */
    private BigDecimal debtAmount = BigDecimal.ZERO;
    /**
     * 供应商欠款
     */
    private BigDecimal debtVAmount = BigDecimal.ZERO;

}
