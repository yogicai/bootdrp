package com.bootdo.se.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class SEOrderVO {
	//单据日期
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date billDate;
    //单据编号
    private String billNo;
    //销售人员ID
    private String billerId;
    //仓库编号
    private String stockNo;
	//客户ID
	private String consumerId;
    //客户名称
    private String consumerName;
	//商品金额
	private BigDecimal entryAmountTotal;
    //合计金额
    private BigDecimal finalAmountTotal;
    //已付金额
    private BigDecimal paymentAmountTotal;
    //优惠率
    private BigDecimal discountRateTotal;
    //优惠金额
    private BigDecimal discountAmountTotal;
    //结算账号
    private String settleAccountTotal;
    //优惠金额
    private BigDecimal debtAccountTotal;
    //客户承担费用
    private BigDecimal expenseFeeTotal;
    //采购费用
    private BigDecimal purchaseFeeTotal;
    //备注
    private String remark;
    //审核状态
    private String auditStatus;

	private final List<SEOrderEntryVO> entryVOList = Lists.newArrayList();


    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public BigDecimal getEntryAmountTotal() {
        return entryAmountTotal;
    }

    public void setEntryAmountTotal(BigDecimal entryAmountTotal) {
        this.entryAmountTotal = entryAmountTotal;
    }

    public BigDecimal getFinalAmountTotal() {
        return finalAmountTotal;
    }

    public void setFinalAmountTotal(BigDecimal finalAmountTotal) {
        this.finalAmountTotal = finalAmountTotal;
    }

    public BigDecimal getPaymentAmountTotal() {
        return paymentAmountTotal;
    }

    public void setPaymentAmountTotal(BigDecimal paymentAmountTotal) {
        this.paymentAmountTotal = paymentAmountTotal;
    }

    public BigDecimal getDiscountRateTotal() {
        return discountRateTotal;
    }

    public void setDiscountRateTotal(BigDecimal discountRateTotal) {
        this.discountRateTotal = discountRateTotal;
    }

    public BigDecimal getDiscountAmountTotal() {
        return discountAmountTotal;
    }

    public void setDiscountAmountTotal(BigDecimal discountAmountTotal) {
        this.discountAmountTotal = discountAmountTotal;
    }

    public String getSettleAccountTotal() {
        return settleAccountTotal;
    }

    public void setSettleAccountTotal(String settleAccountTotal) {
        this.settleAccountTotal = settleAccountTotal;
    }

    public BigDecimal getDebtAccountTotal() {
        return debtAccountTotal;
    }

    public void setDebtAccountTotal(BigDecimal debtAccountTotal) {
        this.debtAccountTotal = debtAccountTotal;
    }

    public BigDecimal getExpenseFeeTotal() {
        return expenseFeeTotal;
    }

    public void setExpenseFeeTotal(BigDecimal expenseFeeTotal) {
        this.expenseFeeTotal = expenseFeeTotal;
    }

    public BigDecimal getPurchaseFeeTotal() {
        return purchaseFeeTotal;
    }

    public void setPurchaseFeeTotal(BigDecimal purchaseFeeTotal) {
        this.purchaseFeeTotal = purchaseFeeTotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<SEOrderEntryVO> getEntryVOList() {
        return entryVOList;
    }
}
