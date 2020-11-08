package com.bootdo.wh.controller.request;

import java.math.BigDecimal;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:44:23
*/
public class WHOrderEntryVO {
    //
    private Integer id;
	//源单编号
	private String billNo;
    //商品ID
    private String entryId;
    //商品名称
    private String entryName;
    //条形码
    private String entryBarcode;
    //商品单位
    private String entryUnit;
    //商品单价
    private BigDecimal entryPrice;
    //仓库编号
    private String stockNo;
    //仓库名称
    private String stockName;
    //数量
    private BigDecimal totalQty;
    //商品金额
    private BigDecimal entryAmount;

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

    public String getEntryBarcode() {
        return entryBarcode;
    }

    public void setEntryBarcode(String entryBarcode) {
        this.entryBarcode = entryBarcode;
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

    public BigDecimal getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(BigDecimal entryAmount) {
        this.entryAmount = entryAmount;
    }
}
