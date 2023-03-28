package com.bootdo.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum BillSource implements EnumBean {

    /** 系统  */
    SYSTEM("系统"),
    USER("用户"),
    IMPORT("导入"),
    ;

    private final String remark;

}
