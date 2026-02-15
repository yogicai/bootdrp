package com.bootdo.modular.workbench.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class CashTrendItem {

    @Schema(description = "日期：yyyyMMid、yyyyMM、yyyy")
    private String oTime;

    @Schema(description = "日期：：dd、MM、yyyy")
    private String time;

    @Schema(description = "营业利润")
    private BigDecimal profitAmount = BigDecimal.ZERO;

    @Schema(description = "营业净现金流")
    private BigDecimal cashFlowAmount = BigDecimal.ZERO;


}
