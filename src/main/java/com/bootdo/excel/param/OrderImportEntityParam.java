package com.bootdo.excel.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/** 
 * 导入订单
 * 
 * @author caiyz
 * @since 2023-03-27 14:53
 */
@Data
public class OrderImportEntityParam {

    /** 客户名称 */
    @Excel(name = "客户名称", width = 15)
    private String consumerName;

    /** 商品名称 */
    @Excel(name = "商品名称", width = 15)
    private String entryName;

    /** 数量 */
    @Excel(name = "销售数量", width = 10)
    private BigDecimal totalQty;

    /** 商品单价 */
    @Excel(name = "零售价", width = 10)
    private BigDecimal entryPrice;

    /** 优惠金额 */
    @Excel(name = "优惠金额", width = 10)
    private BigDecimal discountAmount;

    /** 支付金额 */
    @Excel(name = "支付金额", width = 10)
    private BigDecimal payAmount;

    /** 纸质单据号 */
    @Excel(name = "纸质单据号", width = 15)
    private String paperBillNo;

}
