package com.bootdo.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum BillType implements EnumBean<BillType> {

    /** 采购单  */
    CG_ORDER("采购单"),
    TH_ORDER("退货单"),

    XS_ORDER("销售单"),

    CW_FK_ORDER("付款单"),
    CW_SK_ORDER("收款单"),

    WH_RK_ORDER("入库单"),
    WH_CK_ORDER("出库单");

    private final String remark;

    public static BillType fromValue(String name) {
        for (BillType value : BillType.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
