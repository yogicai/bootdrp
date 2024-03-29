package com.bootdo.modular.po.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 购货订单
 *
 * @author yogiCai
 * @since 2017-11-28 21:30:03
 */
@TableName(value = "po_order")
@Data
public class OrderDO extends BaseEntity {

    private Integer id;
    /**
     * 店铺编号
     */
    @Excel(name = "店铺", dict = "data_shop")
    private String shopNo;

    /**
     * 单据日期
     */
    @Excel(name = "单据日期", exportFormat = "yyyy-MM-dd", width = 15)
    private Date billDate;

    /**
     * 单据编号
     */
    @Excel(name = "单据编号", width = 25)
    private String billNo;

    /**
     * 单据类型
     */
    @Excel(name = "单据类型", enumExportField = "remark")
    private BillType billType;

    /**
     * 供应商ID
     */
    @Excel(name = "供应商ID")
    private String vendorId;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    private String vendorName;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private BigDecimal totalQty;

    /**
     * 商品金额
     */
    @Excel(name = "商品金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal entryAmount;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal discountAmount;

    /**
     * 优惠率
     */
    @Excel(name = "优惠率")
    private BigDecimal discountRate;

    /**
     * 采购费用
     */
    @Excel(name = "采购费用", numFormat = "#,##0.00", width = 15)
    private BigDecimal purchaseFee;

    /**
     * 优惠后金额
     */
    @Excel(name = "优惠后金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal finalAmount;

    /**
     * 合计金额
     */
    @Excel(name = "合计金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal totalAmount;

    /**
     * 已付金额
     */
    @Excel(name = "已付金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal paymentAmount;

    /**
     * 状态
     */
    @Excel(name = "状态", enumExportField = "remark", width = 15)
    private OrderStatus status;

    /**
     * 结算账户
     */
    @Excel(name = "结算账户")
    private String settleAccount;

    /**
     * 制单人
     */
    @Excel(name = "制单人")
    private String billerId;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", enumExportField = "remark")
    private AuditStatus auditStatus;

    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Excel(name = "备注")
    private String remark;

}
