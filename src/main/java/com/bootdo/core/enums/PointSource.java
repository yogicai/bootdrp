package com.bootdo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum PointSource implements EnumBean<PointSource> {

    /**
     * 订单
     */
    ORDER("订单"),
    RETURN("退货"),
    MANUAL("调整");

    private final String remark;

}
