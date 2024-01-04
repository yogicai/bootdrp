package com.bootdo.core.exception;

import com.bootdo.core.exception.assertion.BizServiceAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caiyz
 * @since 2022-03-03 10:09
 */
@Getter
@AllArgsConstructor
public enum BootServiceExceptionEnum implements BizServiceAssert {


    /** 商品库存记录不存在 */
    PRODUCT_COST_NOT_FOUND(7000, "Product cost record not found! productNo:{0}, productName:{1}.", "商品【{1}】库存记录不存在！"),

    /** 单据日期不合法 */
    BILL_DATE_INVALID(7000, "Bill date is not valid! billDate:{0}.", "单据日期不合法！"),

    /** 订单相关 */
    PARAM_INVALID(7001, "Pram not valid! param:{0}.", "参数有误！"),
    STATUS_AUDIT_YES(7002, "Order status cant be audit:{0}.", "已审核的订单不能{0}！"),
    CW_ORDER_REMOVE(7003, "Relate order should be remove before do action! orderType:{0}, billNo:{1}.", "请先删除相关联 {0} {1}！"),
    CW_ORDER_AUDIT(7004, "Order should be cancel audit first!  orderType:{0}, billNo:{1}.", "请先反审核相关联 {0} {1}！"),
    RP_ORDER_AUDIT(7005, "Order should be audit first!  orderType:{0}, billNo:{1}.", "请先审核 {0} {1}！"),
    RP_ORDER_CHECK_ORDER_NULL(7006, "Order amount cant be null for verify order.", "核销订单、核销金额不能为空！"),
    RP_ORDER_SETTLE_ITEM_NULL(7007, "Order amount cant be null.", "收款账户、收款金额不能为空！"),
    DATA_NAME_DUPLICATE(7008, "Duplicate name! type:{0}.", "{0}名称重复！"),
    DATA_NO_DUPLICATE(7009, "Duplicate serial number! type:{0}.", "{0}编号重复！"),

    /** 订单导入 */
    IMPORT_ORDER_NOT_VALID(7000, "Import order not valid! sheetName:{0}, errorMsg:{1}.", "第【{0}】页，</br>{1}！"),
    IMPORT_CONSUMER_NOT_FOUND(7000, "Consumer not found! sheetName:{0}, consumerName:{1}.", "【{0}】页，客户【{1}】不存在！"),
    IMPORT_CONSUMER_NOT_SET(7000, "Consumer not set! sheetName:{0}.", "【{0}】页，客户名称未设置！"),
    IMPORT_PRODUCT_NOT_FOUND(7000, "Product not found! sheetName:{0}, productName:{1}.", "【{0}】页，商品【{1}】不存在！"),



    ;

    /** 异常编码 */
    private final Integer code;
    /** 异常日志输出信息 */
    private final String message;
    /** 异常提示信息 */
    private final String errorMessage;

}