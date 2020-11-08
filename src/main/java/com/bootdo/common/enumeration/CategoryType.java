package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum CategoryType implements EnumBean {

    CUSTOMER("客户类目"),
    VENDOR("供应商类目"),
    PRODUCT("商品类目"),
    PAYMENT("支出类目"),
    INCOME("收入类目"),
    ACCOUNT("帐户类目");

    private final String remark;

    private CategoryType(String remark) {
        this.remark = remark;
    }

    public String remark() {
        return this.remark;
    }

    public String type() {
        return this.name();
    }

    public String toString() {
        return JSON.toJSONString(ImmutableMap.of(name(), remark()));
    }
}
