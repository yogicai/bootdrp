package com.bootdo.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @since 2018-07-07 01:32:23
 */
@AllArgsConstructor
@Getter
public enum InstituteType implements EnumBean<InstituteType> {

    /**
     * 零售客户
     */
    CUSTOMER("零售客户"),
    VENDOR("供应商");

    private final String remark;


    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static InstituteType fromValue(String name) {
        for (InstituteType value : InstituteType.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
