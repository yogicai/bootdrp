package com.bootdo.modular.wh.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@TableName(value = "wh_order")
@Data
public class WHOrderDO extends BaseEntity {

    private Integer id;

    /**
     * 单据日期
     */
    @Excel(name = "单据日期", exportFormat = "yyyy-MM-dd", width = 15)
    private Date billDate;

    /**
     * 单据号
     */
    @Excel(name = "单据号", width = 28)
    private String billNo;

    /**
     * 单据类型
     */
    @Excel(name = "单据类型", enumExportField = "remark")
    private BillType billType;

    /**
     * 业务类型
     */
    @Excel(name = "业务类型", dict = "data_wh_ck,data_wh_rk", width = 12)
    private String serviceType;

    /**
     * 关联单位ID
     */
    @Excel(name = "关联单位ID")
    private String debtorId;

    /**
     * 关联单位名称
     */
    @Excel(name = "关联单位名称")
    private String debtorName;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private BigDecimal totalQty;

    /**
     * 合计金额
     */
    @Excel(name = "合计金额", numFormat = "#,##0.00", width = 15)
    private BigDecimal entryAmount;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", enumExportField = "remark")
    private AuditStatus auditStatus;

    /**
     * 审核人ID
     */
    @Excel(name = "审核人ID")
    private String auditId;

    /**
     * 审核人名称
     */
    @Excel(name = "审核人名称")
    private String auditName;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String operatorId;

    /**
     * 创建人名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Excel(name = "创建人名称")
    private String operatorName;

}
