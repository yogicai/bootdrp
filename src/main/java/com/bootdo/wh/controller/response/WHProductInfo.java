package com.bootdo.wh.controller.response;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class WHProductInfo {
    //商品ID
    @Excel(name = "商品ID", needMerge = true)
    private String entryId;
    //商品名称
    @Excel(name = "商品名称", needMerge = true, width = 20)
    private String entryName;
    //条形码
    @ExcelIgnore
    private String entryBarcode;
    //商品单位
    @Excel(name = "商品单位", needMerge = true)
    private String entryUnit;
    //单位数量
    @Excel(name = "单位数量", needMerge = true)
    private BigDecimal qtyTotal;
    //商品单价
    @Excel(name = "商品单价", needMerge = true)
    private BigDecimal entryPrice;
    //商品金额
    @Excel(name = "商品金额", needMerge = true)
    private BigDecimal entryAmount;
    //库存数量
    @Excel(name = "库存数量", needMerge = true)
    private BigDecimal inventory;
    //库存单位成本
    @Excel(name = "库存单位成本", needMerge = true, numFormat = "#.00")
    private BigDecimal costPrice;
    //库存成本（商品成本 + 费用成本）
    @Excel(name = "库存成本（商品成本 + 费用成本）", needMerge = true)
    private BigDecimal costAmount;

    @ExcelCollection(name = "仓库库存明细")
    private final List<WHStockInfo> stockInfoList = Lists.newArrayList();

    @ExcelIgnore
    @JsonIgnore
    private final Map<String, WHStockInfo> stockInfoMap = Maps.newHashMap();


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

    public BigDecimal getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(BigDecimal qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }

    public BigDecimal getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(BigDecimal entryAmount) {
        this.entryAmount = entryAmount;
    }

    public BigDecimal getInventory() {
        return inventory;
    }

    public void setInventory(BigDecimal inventory) {
        this.inventory = inventory;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public List<WHStockInfo> getStockInfoList() {
        return stockInfoList;
    }

    public Map<String, WHStockInfo> getStockInfoMap() {
        return stockInfoMap;
    }
}
