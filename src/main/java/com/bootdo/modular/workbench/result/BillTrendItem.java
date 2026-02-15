package com.bootdo.modular.workbench.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class BillTrendItem {

    @Schema(description = "日期：yyyyMMid、yyyyMM、yyyy")
    private String oTime;

    @Schema(description = "日期：：dd、MM、yyyy")
    private String time;

    @Schema(description = "单据数据")
    private BigDecimal count = BigDecimal.ZERO;

    @Schema(description = "销售额")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount = BigDecimal.ZERO;

}
