package com.bootdo.rp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 应收、应付票据核销目标单据
 * 
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
public class RPOrderEntryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//应收、应付票据编号
	private String billNo;
	//单据日期
	private Date srcBillDate;
	//单据类型
	private String srcBillType;
	//单据编号
	private String srcBillNo;
	//总欠款
	private BigDecimal srcTotalAmount;
	//已付金额
	private BigDecimal srcPaymentAmount;
	//本次核销金额
	private BigDecimal checkAmount;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：应收、应付票据编号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	/**
	 * 获取：应收、应付票据编号
	 */
	public String getBillNo() {
		return billNo;
	}
	/**
	 * 设置：单据日期
	 */
	public void setSrcBillDate(Date srcBillDate) {
		this.srcBillDate = srcBillDate;
	}
	/**
	 * 获取：单据日期
	 */
	public Date getSrcBillDate() {
		return srcBillDate;
	}
	/**
	 * 设置：单据类型
	 */
	public void setSrcBillType(String srcBillType) {
		this.srcBillType = srcBillType;
	}
	/**
	 * 获取：单据类型
	 */
	public String getSrcBillType() {
		return srcBillType;
	}
	/**
	 * 设置：单据编号
	 */
	public void setSrcBillNo(String srcBillNo) {
		this.srcBillNo = srcBillNo;
	}
	/**
	 * 获取：单据编号
	 */
	public String getSrcBillNo() {
		return srcBillNo;
	}
	/**
	 * 设置：总欠款
	 */
	public void setSrcTotalAmount(BigDecimal srcTotalAmount) {
		this.srcTotalAmount = srcTotalAmount;
	}
	/**
	 * 获取：总欠款
	 */
	public BigDecimal getSrcTotalAmount() {
		return srcTotalAmount;
	}
	/**
	 * 设置：已付金额
	 */
	public void setSrcPaymentAmount(BigDecimal srcPaymentAmount) {
		this.srcPaymentAmount = srcPaymentAmount;
	}
	/**
	 * 获取：已付金额
	 */
	public BigDecimal getSrcPaymentAmount() {
		return srcPaymentAmount;
	}
	/**
	 * 设置：本次核销金额
	 */
	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}
	/**
	 * 获取：本次核销金额
	 */
	public BigDecimal getCheckAmount() {
		return checkAmount;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
