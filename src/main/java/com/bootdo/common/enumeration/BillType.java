package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum BillType implements EnumBean {

    CG_ORDER("采购单"),
    TH_ORDER("退货单"),

    XS_ORDER("销售单"),

    CW_FK_ORDER("付款单"),
    CW_SK_ORDER("收款单"),

    WH_RK_ORDER("入库单"),
    WH_CK_ORDER("出库单");

    private final String remark;

    private BillType(String remark) {
        this.remark = remark;
    }

    public String remark() {
        return this.remark;
    }

    public String type() {
        return this.name();
    }

    public static BillType fromValue(String name) {
        for (BillType value : BillType.values()) {
            if (value.name().equals(name)) return value;
        }
        return null;
    }

    public String toString() {
        return JSON.toJSONString(ImmutableMap.of(name(), remark()));
    }
}
