package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 商品信息表
 *
 * @author yogiCai
 * @since 2017-11-18 22:41:14
 */
@TableName(value = "data_product")
@Data
public class ProductDO extends BaseEntity {
    /**
     *
     */
    @NotNull(groups = {edit.class})
    private Integer id;
    /**
     * 商品编号
     */
    @NotNull(groups = {edit.class})
    private Integer no;
    /**
     * 商品名称
     */
    @NotBlank
    private String name;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 类别
     */
    private String type;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单位名称
     */
    @TableField(exist = false)
    private String unitName;
    /**
     * 库存数量
     */
    @TableField(exist = false)
    private BigDecimal costQty;
    /**
     * 采购价
     */
    private BigDecimal purchasePrice;
    /**
     * 零售价
     */
    private BigDecimal salePrice;
    /**
     * 成本单价
     */
    @TableField(exist = false)
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
     * 状态
     */
    private Integer status = 0;
    /**
     * 店铺编号
     */
    @NotBlank
    private String shopNo;

}
