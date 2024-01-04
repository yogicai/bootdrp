
package com.bootdo.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共状态
 *
 * @author caiyz
 * @since 2023-04-03 20:09
 */
@Getter
@AllArgsConstructor
public enum CommonStatus implements EnumBean<CommonStatus> {

    /**
     * 禁用
     */
    DISABLE(0, "禁用"),

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 删除
     */
    DELETED(2, "删除");

    @EnumValue
    private final Integer code;
    private final String remark;

    public String getValue() {
        return code.toString();
    }

}