package com.bootdo.modular.cashier.enums;

import com.bootdo.core.enums.CommonStatus;
import com.bootdo.core.enums.EnumBean;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日期类型
 *
 * @author L
 * @since 2024-01-15 19:49
 */
@Getter
@AllArgsConstructor
public enum DateTypeEnum implements EnumBean<CommonStatus> {

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

    ;

    private final String remark;


}
