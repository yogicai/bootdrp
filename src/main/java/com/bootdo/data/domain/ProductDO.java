package com.bootdo.data.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 商品信息表
 *
 * @author yogiCai
 * @date 2017-11-18 22:41:14
 */
@Data
public class ProductDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /**  */
    private Integer no;
    /** 商品名称 */
    private String name;
    /** 条形码 */
    private String barCode;
    /** 类别 */
    private String type;
    /** 品牌 */
    private String brand;
    /** 单位 */
    private String unit;
    /** 库存数量 */
    private BigDecimal costQty;
    /** 采购价 */
    private BigDecimal purchasePrice;
    /** 零售价 */
    private BigDecimal salePrice;
    /** 成本单价 */
    private BigDecimal costPrice;
    /** 仓库编号 */
    private String stockNo;
    /** 仓库名称 */
    private String stockName;
    /** 状态 */
    private Integer status = 0;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;

}
