package com.bootdo.modular.engage.result;

import cn.hutool.core.collection.CollUtil;
import com.bootdo.modular.wh.result.WHProductInfo;
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
    private final List<String> stockList = CollUtil.newArrayList();
    /**
     * 商品信息
     */
    private final List<WHProductInfo> productInfoList = CollUtil.newArrayList();

}
