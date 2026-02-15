package com.bootdo.modular.report.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SaleProductItem {

    private Date startDate;

    private Date endDate;

    @Excel(name = "店铺")
    private String shopNo;

    @Excel(name = "商品ID")
    private String entryId;

    @Excel(name = "商品名称")
    private String entryName;

    @Excel(name = "商品单位")
    private String entryUnit;

    @Excel(name = "销售开单量")
    private Integer billCount;

    @Excel(name = "商品均价", numFormat = "#,##0.00")
    private BigDecimal entryPrice;

    @Excel(name = "商品数量", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal totalQty;

    @Excel(name = "销售金额", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal entryAmount;

    @Excel(name = "商品成本", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal costAmount;

    @Excel(name = "销售毛利", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal billProfit;

}
