package com.bootdo.report.enums;

/**
 * @author caiyz
 * @since 2020-11-10 11:00
 */
public enum EChartSeriesType {

    /** 曲线 */
    LINE("line"),
    /** 饼图 */
    BAR("bar");


    private final String value;

    public String value() {
        return value;
    }


    EChartSeriesType(String value) {
        this.value = value;
    }

}