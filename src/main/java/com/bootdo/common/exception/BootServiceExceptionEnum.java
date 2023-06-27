package com.bootdo.common.exception;

import com.bootdo.common.exception.biz.assertion.BizServiceAssert;
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