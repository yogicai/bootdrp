package com.bootdo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum AuditStatus implements EnumBean<AuditStatus> {

    /** 已审核  */
    YES("已审核","审核"),
    NO("未审核","反审核"),
    OTHER("异常状态","异常状态");

    private final String remark;
    private final String remark1;


    public static AuditStatus fromValue(String name) {
        for (AuditStatus value : AuditStatus.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return OTHER;
    }
}
