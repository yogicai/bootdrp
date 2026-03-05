package com.bootdo.modular.rp.enums;

import com.bootdo.core.enums.EnumBean;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author L
 */
@AllArgsConstructor
@Getter
public enum SettleType implements EnumBean<SettleType> {

    PAYEE("收款"),
    DEDUCT("抵扣"),
    ;

    private final String remark;

}
