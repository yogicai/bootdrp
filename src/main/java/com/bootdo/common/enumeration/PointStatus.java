package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum PointStatus implements EnumBean {

    NORMAL("正常"),
    DISABLE("不可用"),
    SETTLE("已结算");

    private final String remark;

    private PointStatus(String remark) {
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
