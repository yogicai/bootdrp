package com.bootdo.rp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:44:23
*/
public class RPOrderEntryVO {
    //
    private Integer id;
	//源单编号
	private String srcBillNo;
	//源单类型
	private String srcBillType;
	//源单据日期
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date srcBillDate;
	//单据金额
	private BigDecimal srcTotalAmount;
    //已核销金额
    private BigDecimal srcPaymentAmount;
	//本次核销金额
	private BigDecimal checkAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSrcBillNo() {
        return srcBillNo;
    }

    public void setSrcBillNo(String srcBillNo) {
        this.srcBillNo = srcBillNo;
    }

    public String getSrcBillType() {
        return srcBillType;
    }

    public void setSrcBillType(String srcBillType) {
        this.srcBillType = srcBillType;
    }

    public Date getSrcBillDate() {
        return srcBillDate;
    }

    public void setSrcBillDate(Date srcBillDate) {
        this.srcBillDate = srcBillDate;
    }

    public BigDecimal getSrcTotalAmount() {
        return srcTotalAmount;
    }

    public void setSrcTotalAmount(BigDecimal srcTotalAmount) {
        this.srcTotalAmount = srcTotalAmount;
    }

    public BigDecimal getSrcPaymentAmount() {
        return srcPaymentAmount;
    }

    public void setSrcPaymentAmount(BigDecimal srcPaymentAmount) {
        this.srcPaymentAmount = srcPaymentAmount;
    }

    public BigDecimal getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(BigDecimal checkAmount) {
        this.checkAmount = checkAmount;
    }
}
