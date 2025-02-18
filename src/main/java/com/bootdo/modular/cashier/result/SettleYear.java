package com.bootdo.modular.cashier.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author L
 * @since 2025-02-18 08:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettleYear {

    @Excel(name = "结算账户名称")
    private String settleName0;
    @Excel(name = "结算账户名称1")
    private String settleName1;
    @Excel(name = "结算账户名称2")
    private String settleName2;
    @Excel(name = "结算账户名称3")
    private String settleName3;

    private List<SettleYearItem> settleYearItemList;


    @Data
    public static class SettleYearItem {
        @Excel(name = "年份")
        private String year;

        @Excel(name = "结算账户名称")
        private String settleName0;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10)
        private BigDecimal checkAmount0;
        @Excel(name = "核销折扣", numFormat = "#,##0.00", type = 10)
        private BigDecimal discountAmount0;

        @Excel(name = "结算账户名称")
        private String settleName1;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10)
        private BigDecimal checkAmount1;
        @Excel(name = "核销折扣", numFormat = "#,##0.00", type = 10)
        private BigDecimal discountAmount1;

        @Excel(name = "结算账户名称")
        private String settleName2;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10)
        private BigDecimal checkAmount2;
        @Excel(name = "核销折扣", numFormat = "#,##0.00", type = 10)
        private BigDecimal discountAmount2;

        @Excel(name = "结算账户名称")
        private String settleName3;
        @Excel(name = "核销金额", numFormat = "#,##0.00", type = 10)
        private BigDecimal checkAmount3;
        @Excel(name = "核销折扣", numFormat = "#,##0.00", type = 10)
        private BigDecimal discountAmount3;

        @Excel(name = "收款金额合计", numFormat = "#,##0.00", type = 10)
        private BigDecimal checkAmountSum;
        @Excel(name = "收款折扣合计", numFormat = "#,##0.00", type = 10)
        private BigDecimal discountAmountSum;
        @Excel(name = "实际收款", numFormat = "#,##0.00", type = 10)
        private BigDecimal paymentAmountSum;
    }
}