package com.bootdo.modular.cashier.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author L
 * @since 2026-02-13 09:39
 */
@Data
public class RecordSum {

    @Schema(description = "记录数量")
    private Integer totalCount;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

}