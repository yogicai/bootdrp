package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum CostVersion implements EnumBean {

    /** 最新成本 */
    CURRENT("最新成本"),
    ;

    private final String remark;

    private CostVersion(String remark) {
        this.remark = remark;
    }

    @Override
    public String remark() {
        return this.remark;
    }

    @Override
    public String type() {
        return this.name();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(ImmutableMap.of(name(), remark()));
    }
}
