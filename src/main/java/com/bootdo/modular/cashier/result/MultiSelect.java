package com.bootdo.modular.cashier.result;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * (CashierRecord)实体类
 *
 * @author makejava
 * @since 2022-06-22 13:21:49
 */
@Data
public class MultiSelect {
    /**
     * 交易渠道
     */
    private Map<String, String> type = new TreeMap<>();
    /**
     * 交易账号
     */
    private Map<String, String> account = new TreeMap<>();

    /**
     * 交易方向
     */
    private Map<String, String> payDirect = new TreeMap<>();

    /**
     * 交易状态
     */
    private Map<String, String> payStatus = new TreeMap<>();

    /**
     * 交易分类
     */
    private Map<String, String> tradeClass = new TreeMap<>();

    /**
     * 数据来源
     */
    private Map<String, String> source = new TreeMap<>();

    /**
     * 资金用途
     */
    private Map<String, String> costType = new TreeMap<>();

}

