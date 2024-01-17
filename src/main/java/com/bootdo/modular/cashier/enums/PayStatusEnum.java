
package com.bootdo.modular.cashier.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态
 *
 * @author L
 * @since 2024-01-15 19:49
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    /**
     * 未支付
     */
    NON_PAY("nonPay", "未支付"),

    /**
     * 已支付
     */
    PAID("paid", "已支付"),

    ;

    @EnumValue
    @JsonValue
    private final String code;

    private final String name;


    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PayStatusEnum fromState(String code) {
        for (PayStatusEnum o : PayStatusEnum.values()) {
            if (o.getCode().equals(code)) {
                return o;
            }
        }
        return null;
    }

}
