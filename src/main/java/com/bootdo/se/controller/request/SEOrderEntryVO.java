package com.bootdo.se.controller.request;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author yogiCai
 * @date 2018-02-01 10:44:23
 */
@Data
public class SEOrderEntryVO {
    /**  */
    private Integer id;
    /** 商品ID */
    private String entryId;
    /** 商品名称 */
    private String entryName;
    /** 商品单位 */
    private String entryUnit;
    /** 商品单价 */
    private BigDecimal entryPrice;
    /** 仓库编号 */
    private String stockNo;
    /** 仓库名称 */
    private String stockName;
    /** 数量 */
    private BigDecimal totalQty;
    /** 金额 */
    private BigDecimal entryAmount;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 优惠率 */
    private BigDecimal discountRate;
    /** 采购费用 */
    private BigDecimal purchaseFee;
    /** 合计金额 */
    private BigDecimal totalAmount;
    /** 备注 */
    private String remark;
    /** 购货订单号 */
    private String requestBillNo;

}
