package com.bootdo.modular.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caiyz
 * @since 2020-11-10 11:00
 */
@Getter
@AllArgsConstructor
public enum EChartSeriesType {

    /** 曲线 */
    LINE("line"),
    /** 饼图 */
    BAR("bar"),

    ;


    private final String value;

}