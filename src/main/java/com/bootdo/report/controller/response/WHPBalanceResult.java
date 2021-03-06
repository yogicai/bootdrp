package com.bootdo.report.controller.response;

import com.bootdo.wh.controller.response.WHProductInfo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class WHPBalanceResult {
    /** 库存日期 */
    private String toDate;
    /** 动态列名 */
    private final List<String> stockList = Lists.newArrayList();
    /** 商品信息 */
	private final List<WHProductInfo> productInfoList = Lists.newArrayList();

}
