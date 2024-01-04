package com.bootdo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum CostVersion implements EnumBean<CostVersion> {

    /** 最新成本 */
    CURRENT("最新成本"),
    ;

    private final String remark;

}
