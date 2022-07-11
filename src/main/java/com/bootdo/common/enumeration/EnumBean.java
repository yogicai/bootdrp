package com.bootdo.common.enumeration;

import cn.hutool.core.util.StrUtil;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
public interface EnumBean <E extends Enum<?>> {
    default String getRemark() {
        return StrUtil.EMPTY;
    };
    default String name() {
        return StrUtil.EMPTY;
    };
}
