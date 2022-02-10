package com.bootdo.wh.controller.response;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;

import java.math.BigDecimal;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class WHStockInfo {
	//仓库编号
    @ExcelIgnore
	private String stockNo;
    //仓库名称
    @Excel(name = "仓库名称", needMerge = true)
    private String stockName;
    //库存数量
    @Excel(name = "库存数量", needMerge = true)
    private BigDecimal totalQty = BigDecimal.ZERO;


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

    public void addTotalQty(BigDecimal totalQty) {
        this.totalQty = this.totalQty.add(totalQty);
    }
}
