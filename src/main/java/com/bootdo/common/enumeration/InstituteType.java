package com.bootdo.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
* @author yogiCai
* @date 2018-07-07 01:32:23
*/
@AllArgsConstructor
@Getter
public enum InstituteType implements EnumBean<InstituteType> {

    /** 零售客户  */
    CUSTOMER("零售客户"),
    VENDOR("供应商");

    private final String remark;

    public static InstituteType fromValue(String name) {
        for (InstituteType value : InstituteType.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
