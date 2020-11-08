package com.bootdo.rp.domain;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 收付款单
 * 
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
public class RPOrderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//单据日期
	private Date billDate;
	//应收、应付票据编号
	private String billNo;
	//单据类型
	private String billType;
	//收付款机构ID
	private String debtorId;
	//收付款机构名称
	private String debtorName;
	//核销人员ID
	private String checkId;
	//核销人员名称
	private String checkName;
	//机构总欠款
	private BigDecimal debtAmount;
	//本次结算金额
	private BigDecimal paymentAmount;
	//本次核销金额
	private BigDecimal checkAmount;
	//整单折扣
	private BigDecimal discountAmount;
	//审核状态
	private String auditStatus;
	//审核人ID
	private String auditId;
	//审核人名称
	private String auditName;
    //单据来源
    private String billSource;
	//备注
	private String remark;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

    private final List<RPOrderEntryDO> entryDOList = Lists.newArrayList();
    private final List<RPOrderSettleDO> settleDOList = Lists.newArrayList();

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
	 * 设置：收付款机构ID
	 */
	public void setDebtorId(String debtorId) {
		this.debtorId = debtorId;
	}
	/**
	 * 获取：收付款机构ID
	 */
	public String getDebtorId() {
		return debtorId;
	}
	/**
	 * 设置：收付款机构名称
	 */
	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}
	/**
	 * 获取：收付款机构名称
	 */
	public String getDebtorName() {
		return debtorName;
	}
	/**
	 * 设置：核销人员ID
	 */
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	/**
	 * 获取：核销人员ID
	 */
	public String getCheckId() {
		return checkId;
	}
	/**
	 * 设置：核销人员名称
	 */
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	/**
	 * 获取：核销人员名称
	 */
	public String getCheckName() {
		return checkName;
	}
	/**
	 * 设置：机构总欠款
	 */
	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
	}
	/**
	 * 获取：机构总欠款
	 */
	public BigDecimal getDebtAmount() {
		return debtAmount;
	}
	/**
	 * 设置：本次结算金额
	 */
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	/**
	 * 获取：本次结算金额
	 */
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
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
	 * 设置：整单折扣
	 */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	/**
	 * 获取：整单折扣
	 */
	public BigDecimal getDiscountAmount() {
		return discountAmount;
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
	 * 设置：审核人ID
	 */
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	/**
	 * 获取：审核人ID
	 */
	public String getAuditId() {
		return auditId;
	}
	/**
	 * 设置：审核人名称
	 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	/**
	 * 获取：审核人名称
	 */
	public String getAuditName() {
		return auditName;
	}
    /**
     * 设置：单据来源
     */
    public String getBillSource() {
        return billSource;
    }
    /**
     * 获取：单据来源
     */
    public void setBillSource(String billSource) {
        this.billSource = billSource;
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

    public List<RPOrderEntryDO> getEntryDOList() {
        return entryDOList;
    }

    public List<RPOrderSettleDO> getSettleDOList() {
        return settleDOList;
    }
}
