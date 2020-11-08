package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
* @Author: yogiCai
* @Date: 2018-07-07 01:32:23
*/
public enum InstituteType implements EnumBean {

    CUSTOMER("零售客户"),
    VENDOR("供应商");

    private final String remark;

    private InstituteType(String remark) {
        this.remark = remark;
    }

    public String remark() {
        return this.remark;
    }

    public String type() {
        return this.name();
    }

    public static InstituteType fromValue(String name) {
        for (InstituteType value : InstituteType.values()) {
            if (value.name().equals(name)) return value;
        }
        return null;
    }

    public String toString() {
        return JSON.toJSONString(ImmutableMap.of(name(), remark()));
    }
}
