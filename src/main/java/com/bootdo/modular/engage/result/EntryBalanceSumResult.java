package com.bootdo.modular.engage.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 商品成本表
 *
 * @author yogiCai
 * @since 2018-03-17 19:35:03
 */
@Data
public class EntryBalanceSumResult {

    @Schema(description = "单据数量")
    private BigDecimal totalCount;

    @Schema(description = "采购、入库数量")
    private BigDecimal incomeQty;

    @Schema(description = "销售、出库数量")
    private BigDecimal outcomeQty;

    @Schema(description = "库存数量")
    private BigDecimal balanceQty;

}
