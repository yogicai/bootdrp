package com.bootdo.modular.se.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 购货订单分录
 *
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */
@Data
public class SEOrderEntryDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /** 单据编号 */
    private String billNo;
    /** 商品ID */
    private String entryId;
    /** 商品名称 */
    private String entryName;
    /** 商品单位 */
    private String entryUnit;
    /** 商品单价 */
    private BigDecimal entryPrice;
    /** 成本单价 */
    private BigDecimal costPrice;
    /** 仓库编号 */
    private String stockNo;
    /** 仓库名称 */
    private String stockName;
    /** 数量 */
    private BigDecimal totalQty;
    /** 商品成本 */
    private BigDecimal costAmount;
    /** 商品金额 */
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
    /** 分录顺序 */
    private Integer sort;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;

}
