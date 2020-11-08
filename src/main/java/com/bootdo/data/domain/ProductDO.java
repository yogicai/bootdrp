package com.bootdo.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 商品信息表
 * @Author: yogiCai
 * @date 2017-11-18 22:41:14
 */
public class ProductDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
    //
    private Integer no;
	//商品名称
	private String name;
	//条形码
	private String barCode;
	//类别
	private String type;
	//品牌
	private String brand;
	//单位
	private String unit;
    //库存数量
    private BigDecimal costQty;
	//采购价
	private BigDecimal purchasePrice;
	//零售价
	private BigDecimal salePrice;
    //成本单价
    private BigDecimal costPrice;
    //仓库编号
    private String stockNo;
    //仓库名称
    private String stockName;
	//状态
	private Integer status = 0;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCostQty() {
        return costQty;
    }

    public void setCostQty(BigDecimal costQty) {
        this.costQty = costQty;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
