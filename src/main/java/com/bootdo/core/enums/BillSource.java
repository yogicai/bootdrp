package com.bootdo.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum BillSource implements EnumBean<BillSource> {

    /**
     * 系统
     */
    SYSTEM("系统"),
    USER("用户"),
    IMPORT("导入"),
    ;

    private final String remark;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BillSource fromCode(String name) {
        for (BillSource o : BillSource.values()) {
            if (o.name().equals(name)) {
                return o;
            }
        }
        return null;
    }

}
