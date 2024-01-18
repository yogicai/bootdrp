package com.bootdo.modular.wh.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 入库出库商品
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Data
public class WHOrderEntryDO implements Serializable {
    /**
     *
     */
    private Integer id;
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 商品ID
     */
    private String entryId;
    /**
     * 商品名称
     */
    private String entryName;
    /**
     * 条形码
     */
    private String entryBarcode;
    /**
     * 商品单位
     */
    private String entryUnit;
    /**
     * 商品单价
     */
    private BigDecimal entryPrice;
    /**
     * 成本单价
     */
    private BigDecimal costPrice;
    /**
     * 仓库编号
     */
    private String stockNo;
    /**
     * 仓库名称
     */
    private String stockName;
    /**
     * 数量
     */
    private BigDecimal totalQty;
    /**
     * 商品金额
     */
    private BigDecimal entryAmount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
