package com.bootdo.report.controller.response;

import com.bootdo.wh.controller.response.WHProductInfo;
import com.google.common.collect.Lists;

import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class WHPBalanceResult {
    /** 库存日期 */
    private String toDate;
    /** 动态列名 */
    private final List<String> stockList = Lists.newArrayList();
    /** 商品信息 */
	private final List<WHProductInfo> productInfoList = Lists.newArrayList();

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<String> getStockList() {
        return stockList;
    }

    public List<WHProductInfo> getProductInfoList() {
        return productInfoList;
    }
}
