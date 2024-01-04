package com.bootdo.modular.wh.controller.request;

import lombok.Data;

import java.math.BigDecimal;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:44:23
*/
@Data
public class WHOrderEntryVO {
    /**  */
    private Integer id;
    /** 源单编号 */
    private String billNo;
    /** 商品ID */
    private String entryId;
    /** 商品名称 */
    private String entryName;
    /** 条形码 */
    private String entryBarcode;
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
    /** 商品金额 */
    private BigDecimal entryAmount;

}
