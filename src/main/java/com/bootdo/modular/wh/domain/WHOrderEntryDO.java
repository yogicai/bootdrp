package com.bootdo.modular.wh.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 入库出库商品
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@TableName(value = "wh_order_entry")
@Data
public class WHOrderEntryDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 店铺编号
     */
    private String shopNo;
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

}
