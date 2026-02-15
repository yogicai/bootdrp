package com.bootdo.modular.workbench.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class CashTotalResult {

    @Schema(description = "营业利润")
    private BigDecimal profitAmountT = BigDecimal.ZERO;

    @Schema(description = "营业净现金流")
    private BigDecimal cashFlowAmountT = BigDecimal.ZERO;


}
