package com.bootdo.modular.se.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
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
 * @since 2018-02-18 16:50:26
 */
@TableName(value = "se_order")
@Data
public class SEOrderDO extends BaseEntity {

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
     * 客户ID
     */
    @Excel(name = "客户ID")
    private String consumerId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String consumerName;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private BigDecimal totalQty;

    /**
     * 商品成本
     */
    @Excel(name = "商品成本", numFormat = "#,##0.00", width = 15)
    private BigDecimal costAmount;

    /**
     * 商品金额
     */
    @Excel(name = "商品金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal entryAmount;

    /**
     * 收款优惠
     */
    @Excel(name = "收款优惠", numFormat = "#,##0.00", width = 15)
    private BigDecimal discountAmount;

    /**
     * 优惠率
     */
    @Excel(name = "优惠率")
    private BigDecimal discountRate;

    /**
     * 客户承担费用
     */
    @Excel(name = "客户承担费用", numFormat = "#,##0.00", width = 15)
    private BigDecimal purchaseFee;

    /**
     * 优惠后金额
     */
    @Excel(name = "优惠后金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal finalAmount;

    /**
     * 已付金额
     */
    @Excel(name = "已付金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal paymentAmount;

    /**
     * 客户承担费用
     */
    @Excel(name = "客户承担费用", numFormat = "#,##0.00", width = 15)
    private BigDecimal expenseFee;

    /**
     * 总欠款
     */
    @Excel(name = "总欠款", numFormat = "#,##0.00", width = 15)
    private BigDecimal totalAmount;

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
     * 销售人员ID
     */
    @Excel(name = "销售人员ID")
    private String billerId;

    /**
     * 销售人员
     */
    @Excel(name = "销售人员")
    private String billerName;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", enumExportField = "remark")
    private AuditStatus auditStatus;

    /**
     * 单据来源
     */
    @Excel(name = "单据来源", enumExportField = "remark")
    private BillSource billSource;

    /**
     * 备注
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Excel(name = "备注")
    private String remark;

}
