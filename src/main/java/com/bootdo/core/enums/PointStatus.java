package com.bootdo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum PointStatus implements EnumBean<PointStatus> {

    /** 正常 */
    NORMAL("正常"),
    DISABLE("不可用"),
    SETTLE("已结算");

    private final String remark;

}
