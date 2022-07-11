package com.bootdo.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yogiCai
 * @date 2018-01-21 11:45:46
 */
@AllArgsConstructor
@Getter
public enum CategoryType implements EnumBean {

    /** 客户类目  */
    CUSTOMER("客户类目"),
    VENDOR("供应商类目"),
    PRODUCT("商品类目"),
    PAYMENT("支出类目"),
    INCOME("收入类目"),
    ACCOUNT("帐户类目");

    private final String remark;

}
