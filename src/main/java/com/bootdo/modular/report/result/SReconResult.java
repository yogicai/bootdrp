package com.bootdo.modular.report.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @date 2018-02-01 10:43:43
 */
@Data
public class SReconResult {

    @Excel(name = "编号")
    private String instituteId;

    @Excel(name = "客户名称")
    private String instituteName;

    @Excel(name = "年度", width = 25)
    private String billRegion;

    @Excel(name = "应收金额", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal totalAmount;

    @Excel(name = "收款金额", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal paymentAmount;

    @Excel(name = "商品成本", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal costAmount;

    @Excel(name = "销售毛利", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal profitAmount;

    @Excel(name = "欠款金额", numFormat = "#,##0.00", isStatistics = true, width = 15)
    private BigDecimal debtAmount;

}
