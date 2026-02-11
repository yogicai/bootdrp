package com.bootdo.modular.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author L
 * @since 2020-11-10 11:00
 */
@Getter
@AllArgsConstructor
public enum DimensionType {

    /**
     * 年
     */
    YEAR("年"),
    /**
     * 月
     */
    MONTH("月"),
    /**
     * 日
     */
    DAY("日"),
    /**
     * 用户
     */
    USER("用户"),

    ;

    private final String remark;

}