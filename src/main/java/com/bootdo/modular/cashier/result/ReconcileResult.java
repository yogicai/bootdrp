package com.bootdo.modular.cashier.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * 收款对账
 *
 * @author L
 * @since 2025-03-19 15:11
 */
@Data
public class ReconcileResult {

    private String start;

    private String end;

    @Schema(description = "单据数量")
    private Integer billCount;

    @Schema(description = "核销金额")
    private BigDecimal checkAmountTotal;

    @Schema(description = "收款金额")
    private BigDecimal paymentAmountTotal;

    @Schema(description = "到账金额")
    private BigDecimal payAmountTotal;

    private Collection<ReconcileItem> reconcileItemList;


    @Data
    public static class ReconcileItem {
        @Excel(name = "单据日期", width = 15)
        private String billDate;
        @Excel(name = "单据编号", width = 35)
        private String billNo;
        @Excel(name = "单据数量", width = 15)
        private Integer billCount;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal checkAmount;
        @Excel(name = "收款金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal paymentAmount;
        @Excel(name = "到账金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal payAmount;
    }

    @Data
    public static class ReconcileItemSum {
        @Excel(name = "单据数量", width = 15)
        private Integer billCountTotal;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal checkAmountTotal;
        @Excel(name = "收款金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal paymentAmountTotal;
        @Excel(name = "到账金额", numFormat = "#,##0.00", type = 10, width = 15)
        private BigDecimal payAmountTotal;
    }

}
