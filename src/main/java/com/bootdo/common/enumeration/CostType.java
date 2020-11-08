package com.bootdo.common.enumeration;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

/**
 * @Author: yogiCai
 * @Date: 2018-01-21 11:45:46
 */
public enum CostType implements EnumBean {

    PO_CG("采购单调整"),
    PO_TH("采购退货单调整"),

    WH_RK("入库单调整"),
    WH_CK("出库单调整");

    private final String remark;

    private CostType(String remark) {
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
