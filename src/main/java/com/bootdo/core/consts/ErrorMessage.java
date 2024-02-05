package com.bootdo.core.consts;

/**
 * @Author: yogiCai
 * @since 2018-02-04 10:27:25
 */
public final class ErrorMessage {

    //参数相关.
    public static final String PARAM_INVALID = "参数有误";
    public static final String STATUS_AUDIT_YES = "已审核的订单不能%s";
    public static final String CW_ORDER_REMOVE = "请先删除相关联%s %s";
    public static final String CW_ORDER_AUDIT = "请先反审核相关联 %s %s";
    public static final String RP_ORDER_AUDIT = "请先审核 %s %s";
    public static final String RP_ORDER_CHECK_ORDER_NULL = "核销订单、核销金额不能为空";
    public static final String RP_ORDER_SETTLE_ITEM_NULL = "收款账户、收款金额不能为空";
    public static final String DATA_NAME_DUPLICATE = "%s名称重复";
    public static final String DATA_NO_DUPLICATE = "%s编号重复";
    public static final String SIGNATURE_MISMATCHES = "请求内容与签名不匹配";
    public static final String ORDER_ITEM_NOT_MATCH = "Order和OrderItem不匹配";
    public static final String OPERATE_ROLE_ERROR = "订单操作人错误";
    public static final String ORDER_ITEM_NOT_EXIST = "子订单不存在";
    public static final String ORDER_VOUCHER_NOT_EXISTS = "代金券商品订单不存在";
    public static final String ORDER_NOT_EXIST = "订单不存在";
    public static final String PRODUCT_ACCOUNT_MUST_INTEGER = "订单为标品则productAccount必须为整数";
    public static final String PRODUCT_ACCOUNT_MAN_SCALE = "订单为非标品则productAccount小数位最多2位";
    public static final String START_TIME_MUST_BEFORE_END_TIME = "起始时间必须小于截止时间";
    public static final String SEARCH_ORDER_INFO_PARAM_INVALID = "查询订单详情，orderId、orderNo、tradeNo至少传一个，不能全部为空";
    public static final String SHOP_ID_IS_NULL = "当前shopId不合法，为空或者为零";
    public static final String USER_ID_IS_NULL = "当前userId不合法，为空或者为零";
    public static final String ORDER_SHOP_ID_NO_MATCH = "当前shopId不匹配";
    public static final String ORDER_USER_MOBILE_NO_MATCH = "当前mobile不匹配";

}
