package com.bootdo.modular.cashier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * excel chart
 *
 * @author L
 * @since 2024-01-15 19:49
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {

    /**
     * 图表类目（X轴）
     */
    CHART_CATEGORY(DataTypeEnum.CONST_CHART_CATEGORY, "X轴"),
    /**
     * 图表数值（Y轴）
     */
    CHART_DATA(DataTypeEnum.CONST_CHART_DATA, "Y轴"),

    ;

    private final String code;
    private final String name;

    public static final String CONST_CHART_CATEGORY = "chartCategory";
    public static final String CONST_CHART_DATA = "chartData";

    public boolean equalCode(String code) {
        return Arrays.stream(DataTypeEnum.values()).anyMatch(e -> e.getCode().equals(code));
    }

    public static DataTypeEnum fromCode(String code) {
        for (DataTypeEnum o : DataTypeEnum.values()) {
            if (o.getCode().equals(code)) {
                return o;
            }
        }
        return null;
    }

}
