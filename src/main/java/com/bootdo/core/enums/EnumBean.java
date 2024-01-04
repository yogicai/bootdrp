package com.bootdo.core.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
public interface EnumBean <E extends Enum<?>> {

    /**
     * 下拉选择框 展示文本
     * @return  String
     */
    default String getRemark() {
        return StrUtil.EMPTY;
    };

    /**
     * 下拉选择框 默认值
     * @return  String
     */
    default String name() {
        return StrUtil.EMPTY;
    };

    /**
     * 下拉选择框 值
     * @return  String
     */
    default String getValue() {
        return this.name();
    };

}
