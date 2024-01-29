package com.bootdo.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共状态
 *
 * @author L
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
    @JsonValue
    private final Integer code;
    private final String remark;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CommonStatus fromCode(Integer code) {
        for (CommonStatus o : CommonStatus.values()) {
            if (o.getCode().equals(code)) {
                return o;
            }
        }
        return null;
    }

    public String getValue() {
        return code.toString();
    }

}