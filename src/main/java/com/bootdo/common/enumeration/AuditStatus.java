package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum AuditStatus implements EnumBean {

    YES("已审核","审核"),
    NO("未审核","反审核"),
    OTHER("异常状态","异常状态");

    private final String remark;
    private final String remark1;

    private AuditStatus(String remark, String remark1) {
        this.remark = remark;
        this.remark1 = remark1;
    }

    public String remark() {
        return this.remark;
    }

    public String remark1() {
        return this.remark1;
    }

    public String type() {
        return this.name();
    }

    public String toString() {
        return JSON.toJSONString(ImmutableMap.of(name(), remark()));
    }

    public static AuditStatus fromValue(String name) {
        for (AuditStatus value : AuditStatus.values()) {
            if (value.name().equals(name)) return value;
        }
        return OTHER;
    }
}
