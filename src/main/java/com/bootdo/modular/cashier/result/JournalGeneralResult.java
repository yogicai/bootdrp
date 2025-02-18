package com.bootdo.modular.cashier.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.enums.DataTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导入订单
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class JournalGeneralResult {

    /**
     * 统计起始
     */
    private String startYear;
    /**
     * 统计结止
     */
    private String endYear;

    /**
     * 账户现金流合计
     */
    private Collection<AccountItem> flowAccountStatList;
    /**
     * 年份现金流合计
     */
    private Collection<AccountItem> flowAccountYearList;
    /**
     * 经营情况
     */
    private Collection<OperateItem> operateYearList;
    /**
     * 年份核销金额合计
     */
    private SettleYear settleYear;

    @JsonIgnore
    @ExcelCollection(name = "账户现金流水", type = RecordDO.class)
    private Map<String, List<RecordDO>> flowRecordMap;

    @JsonIgnore
    @ExcelCollection(name = "月现金流", type = OperateMonthItem.class)
    private Collection<OperateMonthItem> operateMonthList;

    @JsonIgnore
    @ExcelCollection(name = "欠款明细", type = DebtItem.class)
    private Collection<DebtItem> debtItemList;

    @JsonIgnore
    @ExcelCollection(name = "收款明细", type = SettleOrderItem.class)
    private Collection<SettleOrderItem> settleOrderItemList;


    /**
     * 现金流
     */
    @NoArgsConstructor
    @Data
    public static class AccountItem {
        @Excel(name = "年份")
        private String year;
        @Excel(name = "账户")
        private String account;
        @Excel(name = "打款", numFormat = "#,##0.00", type = 10)
        private BigDecimal remit;
        @Excel(name = "经销商", numFormat = "#,##0.00", type = 10)
        private BigDecimal agency;
        @Excel(name = "调货", numFormat = "#,##0.00", type = 10)
        private BigDecimal exchange;
        @Excel(name = "调账", numFormat = "#,##0.00", type = 10)
        private BigDecimal adjust;
        @Excel(name = "店内调账", numFormat = "#,##0.00", type = 10)
        private BigDecimal innerAdjust;
        @Excel(name = "父母", numFormat = "#,##0.00", type = 10)
        private BigDecimal parent;
        @Excel(name = "个人消费", numFormat = "#,##0.00", type = 10)
        private BigDecimal consume;
        @Excel(name = "换现金", numFormat = "#,##0.00", type = 10)
        private BigDecimal forCash;
        @Excel(name = "贷款综合", numFormat = "#,##0.00", type = 10)
        private BigDecimal loans;
        @Excel(name = "生活开支", numFormat = "#,##0.00", type = 10)
        private BigDecimal living;
        @Excel(name = "运营费用", numFormat = "#,##0.00", type = 10)
        private BigDecimal operation;
        @Excel(name = "支付工资", numFormat = "#,##0.00", type = 10)
        private BigDecimal salary;
        @Excel(name = "营销费用", numFormat = "#,##0.00", type = 10)
        private BigDecimal market;

        public AccountItem(String year, String account) {
            this.year = year;
            this.account = account;
        }

    }

    /**
     * 销售情况
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperateItem {
        @Excel(name = "年份")
        private String year;
        @Excel(name = "营业额", numFormat = "0.00", type = 10)
        private BigDecimal totalAmount;
        @Excel(name = "收款", numFormat = "0.00", type = 10)
        private BigDecimal paymentAmount;
        @Excel(name = "成本", numFormat = "0.00", type = 10)
        private BigDecimal costAmount;
        @Excel(name = "欠款", numFormat = "0.00", type = 10)
        private BigDecimal debtAmount;
        @Excel(name = "毛利", numFormat = "0.00", type = 10)
        private BigDecimal profitAmount;
        @Excel(name = "毛利率", numFormat = "#,##0.00%", type = 10)
        private BigDecimal profitRate;
        @Excel(name = "父母", numFormat = "0.00", type = 10)
        private BigDecimal parentFee;
        @Excel(name = "生活开支", numFormat = "0.00", type = 10)
        private BigDecimal livingFee;
        @Excel(name = "运营费用", numFormat = "0.00", type = 10)
        private BigDecimal operationFee;
        @Excel(name = "工资", numFormat = "0.00", type = 10)
        private BigDecimal salaryFee;
        @Excel(name = "营销费用", numFormat = "0.00", type = 10)
        private BigDecimal marketFee;
        @Excel(name = "净利润", numFormat = "0.00", type = 10)
        private BigDecimal netProfit;
        @Excel(name = "净利润率", numFormat = "#,##0.00%", type = 10)
        private BigDecimal netProfitRate;

        @ExcelIgnore
        @Excel(name = "商品金额", numFormat = "0.00", type = 10)
        private BigDecimal entryAmount;
        @ExcelIgnore
        @Excel(name = "折扣金额", numFormat = "0.00", type = 10)
        private BigDecimal discountAmount;
        @ExcelIgnore
        @Excel(name = "折扣后金额", numFormat = "0.00", type = 10)
        private BigDecimal finalAmount;
        @ExcelIgnore
        @Excel(name = "已实现净利润", numFormat = "0.00", type = 10)
        private BigDecimal netProfitRealized;
        @ExcelIgnore
        @Excel(name = "已支付工资", numFormat = "0.00", type = 10)
        private BigDecimal salaryRealized;
    }

    /**
     * 月销售情况
     * 标记图表：
     * 1、X轴数据：DataTypeEnum.CONST_CHART_CATEGORY
     * 2、Y轴数据：DataTypeEnum.CONST_CHART_DATA
     */
    @Data
    public static class OperateMonthItem {
        @Excel(name = "年份", dict = DataTypeEnum.CONST_CHART_CATEGORY)
        private String year;
        @Excel(name = "销售数量", numFormat = "0", type = 10, orderNum = "1")
        private Integer totalQty;
        @Excel(name = "营业额", numFormat = "0.00", type = 10, width = 12, orderNum = "2", dict = DataTypeEnum.CONST_CHART_DATA)
        private BigDecimal totalAmount;
        @Excel(name = "收款", numFormat = "0.00", type = 10, width = 12, orderNum = "3", dict = DataTypeEnum.CONST_CHART_DATA)
        private BigDecimal paymentAmount;
        @Excel(name = "成本", numFormat = "0.00", type = 10, width = 12, orderNum = "4", dict = DataTypeEnum.CONST_CHART_DATA)
        private BigDecimal costAmount;
        @Excel(name = "欠款", numFormat = "0.00", type = 10, width = 12, orderNum = "5")
        private BigDecimal debtAmount;
        @Excel(name = "毛利", numFormat = "0.00", type = 10, width = 12, orderNum = "6")
        private BigDecimal profitAmount;
        @Excel(name = "已实现利润", numFormat = "0.00", type = 10, width = 12, orderNum = "7", dict = DataTypeEnum.CONST_CHART_DATA)
        private BigDecimal netAmount;
        @Excel(name = "订单数", numFormat = "0", type = 10, width = 8, orderNum = "8")
        private Integer billCount;
    }

    /**
     * 欠款情况
     */
    @Data
    public static class DebtItem {
        @Excel(name = "用户")
        private String instituteId;
        @Excel(name = "用户名", width = 15)
        private String instituteName;
        @Excel(name = "单据日期", width = 25)
        private String billRegion;
        @Excel(name = "营业额", numFormat = "0.00", type = 10, width = 12)
        private BigDecimal totalAmount;
        @Excel(name = "成本", numFormat = "0.00", type = 10, width = 12)
        private BigDecimal costAmount;
        @Excel(name = "收款", numFormat = "0.00", type = 10, width = 12)
        private BigDecimal paymentAmount;
        @Excel(name = "欠款", numFormat = "0.00", type = 10, width = 12)
        private BigDecimal debtAmount;
        @Excel(name = "毛利", numFormat = "0.00", type = 10, width = 12)
        private BigDecimal profitAmount;
        @Excel(name = "订单数", numFormat = "0", type = 10, width = 8)
        private Integer billCount;
    }

    /**
     * 工资
     */
    @Data
    public static class SalaryRecord {
        @Excel(name = "年份")
        private String year;
        @Excel(name = "工资金额", numFormat = "0.00", type = 10)
        private BigDecimal totalAmount;
    }


    /**
     * 订单核销明细
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SettleOrderItem {
        @Excel(name = "单据日期", exportFormat = "yyyy-MM-dd")
        private Date billDate;
        @Excel(name = "订单编号")
        private String billNo;
        @Excel(name = "营业额", numFormat = "0.00", type = 10)
        private BigDecimal totalAmount;
        @Excel(name = "核销金额", numFormat = "0.00", type = 10)
        private BigDecimal checkAmount;
        @Excel(name = "核销折扣", numFormat = "0.00", type = 10)
        private BigDecimal discountAmount;
        @Excel(name = "收款金额", numFormat = "0.00", type = 10)
        private BigDecimal settleAmount;
        @Excel(name = "结算账户")
        private String settleAccount;
        @Excel(name = "结算账户名称")
        private String settleName;
    }

}
