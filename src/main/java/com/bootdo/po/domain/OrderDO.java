package com.bootdo.po.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 购货订单
 * @Author: yogiCai
 * @date 2017-11-28 21:30:03
 */
public class OrderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//单据日期
	private Date billDate;
	//单据编号
	private String billNo;
	//单据类型
	private String billType;
	//供应商ID
	private String vendorId;
    //供应商名称
    private String vendorName;
	//数量
	private BigDecimal totalQty;
	//商品金额
	private BigDecimal entryAmount;
	//优惠金额
	private BigDecimal discountAmount;
	//优惠率
	private BigDecimal discountRate;
	//采购费用
	private BigDecimal purchaseFee;
    //优惠后金额
    private BigDecimal finalAmount;
    //合计金额
    private BigDecimal totalAmount;
    //已付金额
    private BigDecimal paymentAmount;
	//状态
	private String status;
	//结算账户
	private String settleAccount;
	//制单人
	private String billerId;
	//审核状态
	private String auditStatus;
	//备注
	private String remark;
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
	 * 设置：单据日期
	 */
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	/**
	 * 获取：单据日期
	 */
	public Date getBillDate() {
		return billDate;
	}
	/**
	 * 设置：单据编号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	/**
	 * 获取：单据编号
	 */
	public String getBillNo() {
		return billNo;
	}
	/**
	 * 设置：单据类型
	 */
	public void setBillType(String billType) {
		this.billType = billType;
	}
	/**
	 * 获取：单据类型
	 */
	public String getBillType() {
		return billType;
	}
	/**
	 * 设置：供应商ID
	 */
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * 获取：供应商ID
	 */
	public String getVendorId() {
		return vendorId;
	}
    /**
     * 设置：供应商名称
     */
    public String getVendorName() {
        return vendorName;
    }
    /**
     * 获取：供应商名称
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    /**
	 * 设置：数量
	 */
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	/**
	 * 获取：数量
	 */
	public BigDecimal getTotalQty() {
		return totalQty;
	}
    /**
     * 设置：商品金额
     */
    public BigDecimal getEntryAmount() {
        return entryAmount;
    }
    /**
     * 获取：商品金额
     */
    public void setEntryAmount(BigDecimal entryAmount) {
        this.entryAmount = entryAmount;
    }
	/**
	 * 设置：合计金额
	 */
	public void setFinalAmount(BigDecimal entryAmount) {
		this.finalAmount = entryAmount;
	}
	/**
	 * 获取：合计金额
	 */
	public BigDecimal getFinalAmount() {
		return finalAmount;
	}
    /**
     * 设置：已付金额
     */
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    /**
     * 设置：合计金额
     */
    public void setTotalAmount(BigDecimal entryAmount) {
        this.totalAmount = entryAmount;
    }
    /**
     * 获取：合计金额
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    /**
     * 获取：已付金额
     */
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    /**
	 * 设置：优惠金额
	 */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	/**
	 * 获取：优惠金额
	 */
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	/**
	 * 设置：优惠率
	 */
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	/**
	 * 获取：优惠率
	 */
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	/**
	 * 设置：采购费用
	 */
	public void setPurchaseFee(BigDecimal purchaseFee) {
		this.purchaseFee = purchaseFee;
	}
	/**
	 * 获取：采购费用
	 */
	public BigDecimal getPurchaseFee() {
		return purchaseFee;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：结算账户
	 */
	public void setSettleAccount(String settleAccount) {
		this.settleAccount = settleAccount;
	}
	/**
	 * 获取：结算账户
	 */
	public String getSettleAccount() {
		return settleAccount;
	}
	/**
	 * 设置：制单人
	 */
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	/**
	 * 获取：制单人
	 */
	public String getBillerId() {
		return billerId;
	}
	/**
	 * 设置：审核状态
	 */
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * 获取：审核状态
	 */
	public String getAuditStatus() {
		return auditStatus;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
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
