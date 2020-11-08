package com.bootdo.rp.controller.request;

import java.math.BigDecimal;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:44:23
*/
public class RPOrderSettleVO {
    //
    private Integer id;
	//商品ID
	private String settleAccount;
    //收款金额
	private BigDecimal paymentAmount;
	//备注
	private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSettleAccount() {
        return settleAccount;
    }

    public void setSettleAccount(String settleAccount) {
        this.settleAccount = settleAccount;
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
}
