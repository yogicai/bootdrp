package com.bootdo.modular.engage.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 商品成本表
 *
 * @author yogiCai
 * @since 2018-03-17 19:35:03
 */
@TableName(value = "data_product_cost")
@Data
public class ProductCostDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 店铺编号
     */
    @Excel(name = "shopNo")
    private String shopNo;
    /**
     * 商品编号
     */
    @Excel(name = "商品编号")
    private String productNo;
    /**
     * 商品名称
     */
    @TableField(exist = false)
    @Excel(name = "商品名称", width = 20)
    private String productName;
    /**
     * 商品类型
     */
    @TableField(exist = false)
    @Excel(name = "商品类型", dict = "PRODUCT")
    private String productType;
    /**
     * 商品金额
     */
    @Excel(name = "采购价", numFormat = "#,##0.00", width = 15)
    private BigDecimal entryPrice;
    /**
     * 成本单价
     */
    @Excel(name = "成本单价", numFormat = "#,##0.00", width = 15)
    private BigDecimal costPrice;
    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    private BigDecimal costQty;
    /**
     * 库存变更
     */
    @Excel(name = "库存变更")
    private BigDecimal entryQty;
    /**
     * 库存余额(此次库存变化时刻的库存数量)
     */
    @Excel(name = "商品成本", numFormat = "#,##0.00", width = 15)
    private BigDecimal costBalance;
    /**
     * 库存成本
     */
    @Excel(name = "商品成本", numFormat = "#,##0.00", width = 15)
    private BigDecimal costAmount;
    /**
     * 估算时间
     */
    @Excel(name = "估算时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private Date costDate;
    /**
     * 成本类型
     */
    @Excel(name = "成本类型", dict = "COST_TYPE", width = 15)
    private String costType;
    /**
     * 关联单号
     */
    @Excel(name = "关联单号", width = 25)
    private String relateNo;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 25)
    private String remark;

}
