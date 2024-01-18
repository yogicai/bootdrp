package com.bootdo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum CostType implements EnumBean<CostType> {

    /**
     * 采购单调整
     */
    PO_CG("采购单调整"),
    PO_TH("采购退货单调整"),

    SE_XS("销售单调整"),

    WH_RK("入库单调整"),
    WH_CK("出库单调整"),

    MANUAL("手工调整"),
    ;

    private final String remark;

}
