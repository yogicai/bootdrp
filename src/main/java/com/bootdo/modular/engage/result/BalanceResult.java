package com.bootdo.modular.engage.result;

import com.bootdo.modular.wh.result.WHProductInfo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class BalanceResult {
    /**
     * 库存日期
     */
    private String toDate;
    /**
     * 动态列名
     */
    private final List<String> stockList = Lists.newArrayList();
    /**
     * 商品信息
     */
    private final List<WHProductInfo> productInfoList = Lists.newArrayList();

}
