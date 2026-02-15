package com.bootdo.modular.workbench.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class HisPBillTrendItem {

    @Schema(description = "日期：yyyyMMid、yyyyMM、yyyy")
    private String oTime;

    @Schema(description = "日期：：dd、MM、yyyy")
    private String time;

    @Schema(description = "单据数据")
    private Integer count = 0;

    @Schema(description = "销售额")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount = BigDecimal.ZERO;

    @Schema(description = "成本金额")
    private BigDecimal costAmount = BigDecimal.ZERO;

    @Schema(description = "欠款金额")
    private BigDecimal debtAmount = BigDecimal.ZERO;

    @Schema(description = "营业利润")
    private BigDecimal profitAmount = BigDecimal.ZERO;

}
