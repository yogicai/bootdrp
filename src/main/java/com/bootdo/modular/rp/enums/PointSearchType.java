package com.bootdo.modular.rp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author L
 */
@AllArgsConstructor
@Getter
public enum PointSearchType {

    COLLECT("汇总"),
    DETAIL("明细"),
    INCOME("收入"),
    OUTCOME("支出"),
    ;

    private final String remark;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PointSearchType fromCode(String name) {
        for (PointSearchType o : PointSearchType.values()) {
            if (o.name().equals(name)) {
                return o;
            }
        }
        return null;
    }

}
