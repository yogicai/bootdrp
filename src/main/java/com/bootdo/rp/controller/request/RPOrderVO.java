package com.bootdo.rp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class RPOrderVO {
	//单据日期
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date billDate;
    //单据编号
    private String billNo;
    //收款、付款标识
    private String billType;
	//买家ID
	private String debtorId;
    //买家名称
    private String debtorName;
    //收款人ID
    private String checkId;
    //收款人名称
    private String checkName;
	//整单折扣
	private BigDecimal discountAmount;
    //备注
    private String remark;
    //审核状态
    private String auditStatus;

    //结算金额明细
    private final List<RPOrderSettleVO> settleVOList = Lists.newArrayList();
    //源单及核销金额明细
	private final List<RPOrderEntryVO> entryVOList = Lists.newArrayList();


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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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

    public List<RPOrderSettleVO> getSettleVOList() {
        return settleVOList;
    }

    public List<RPOrderEntryVO> getEntryVOList() {
        return entryVOList;
    }
}
