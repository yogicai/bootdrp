package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum PointSource implements EnumBean {

    ORDER("订单"),
    RETURN("退货"),
    MANUAL("调整");

    private final String remark;

    private PointSource(String remark) {
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
