package com.bootdo.report.controller.response;

import lombok.Data;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class SEBillTotalResult {
    /** 销售总额 */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /** 毛利 */
    private BigDecimal profit = BigDecimal.ZERO;

}
