package com.bootdo.rp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 应收、应付票据结算表
 * 
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
public class RPOrderSettleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//应收、应付票据编号
	private String billNo;
	//结算账户
	private String settleAccount;
    //结算账户名称
    private String settleName;
	//已付金额
	private BigDecimal paymentAmount;
	//备注
	private String remark;
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

    public String getSettleAccount() {
        return settleAccount;
    }

    public void setSettleAccount(String settleAccount) {
        this.settleAccount = settleAccount;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
