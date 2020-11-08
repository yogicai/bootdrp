package com.bootdo.se.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 购货订单分录
 * 
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */
public class SEOrderEntryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//单据编号
	private String billNo;
	//商品ID
	private String entryId;
	//商品名称
	private String entryName;
	//商品单位
	private String entryUnit;
	//商品单价
	private BigDecimal entryPrice;
    //成本单价
    private BigDecimal costPrice;
	//仓库编号
	private String stockNo;
	//仓库名称
	private String stockName;
	//数量
	private BigDecimal totalQty;
    //商品成本
    private BigDecimal costAmount;
	//商品金额
	private BigDecimal entryAmount;
	//优惠金额
	private BigDecimal discountAmount;
	//优惠率
	private BigDecimal discountRate;
	//采购费用
	private BigDecimal purchaseFee;
	//合计金额
	private BigDecimal totalAmount;
	//备注
	private String remark;
	//购货订单号
	private String requestBillNo;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryUnit() {
        return entryUnit;
    }

    public void setEntryUnit(String entryUnit) {
        this.entryUnit = entryUnit;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(BigDecimal entryAmount) {
        this.entryAmount = entryAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getPurchaseFee() {
        return purchaseFee;
    }

    public void setPurchaseFee(BigDecimal purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestBillNo() {
        return requestBillNo;
    }

    public void setRequestBillNo(String requestBillNo) {
        this.requestBillNo = requestBillNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
