package com.bootdo.modular.engage.controller.response;

import lombok.Data;

import java.math.BigDecimal;


/**
* @author yogiCai
* @date 2018-02-01 10:43:43
*/
@Data
public class BalanceTotalResult {
    /** 库存余量 */
    private BigDecimal qtyTotal = BigDecimal.ZERO;
    /** 成本（商品成本 + 费用成本） */
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
