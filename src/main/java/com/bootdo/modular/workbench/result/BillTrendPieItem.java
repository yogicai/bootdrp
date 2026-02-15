package com.bootdo.modular.workbench.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class BillTrendPieItem {

    @Schema(description = "商品类别编号")
    private String type;

    @Schema(description = "商品类别名称")
    private String name;

    @Schema(description = "单据数据")
    private Integer count = 0;

    @Schema(description = "毛利")
    private BigDecimal profitAmount = BigDecimal.ZERO;

    @Schema(description = "销售额")
    private BigDecimal totalAmount = BigDecimal.ZERO;


}
