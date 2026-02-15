package com.bootdo.modular.cashier.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author L
 * @since 2026-02-13 09:39
 */
@Data
public class FlowRecordItem {

    @Schema(description = "年份")
    private String year;

    @Schema(description = "结算账户名称")
    private String account;

    @Schema(description = "支出类型")
    private String costType;

    @Schema(description = "金额")
    private BigDecimal payAmount = BigDecimal.ZERO;

}