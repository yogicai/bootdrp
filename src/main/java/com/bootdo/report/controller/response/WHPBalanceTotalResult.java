package com.bootdo.report.controller.response;

import lombok.Data;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class WHPBalanceTotalResult {
    /** 库存余量 */
    private BigDecimal qtyTotal = BigDecimal.ZERO;
    /** 成本（商品成本 + 费用成本） */
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
