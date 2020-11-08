package com.bootdo.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 商品成本表
 * 
 * @author yogiCai
 * @email 1992lcg@163.com
 * @date 2018-03-17 19:35:03
 */
public class ProductCostDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//商品编号
	private String productNo;
    //商品金额
    private BigDecimal entryPrice;
	//成本单价
	private BigDecimal costPrice;
    //库存数量
    private BigDecimal costQty;
    //库存余额(此次库存变化时刻的库存数量)
    private BigDecimal costBalance;
    //库存成本
    private BigDecimal costAmount;
	//估算时间
	private Date costDate;
	//成本类型
	private String costType;
    //关联单号
    private String relateNo;
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

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
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

    public BigDecimal getCostQty() {
        return costQty;
    }

    public void setCostQty(BigDecimal costQty) {
        this.costQty = costQty;
    }

    public BigDecimal getCostBalance() {
        return costBalance;
    }

    public void setCostBalance(BigDecimal costBalance) {
        this.costBalance = costBalance;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getRelateNo() {
        return relateNo;
    }

    public void setRelateNo(String relateNo) {
        this.relateNo = relateNo;
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
