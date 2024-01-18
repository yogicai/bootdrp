package com.bootdo.modular.cashier.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import com.bootdo.modular.cashier.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工资表
 *
 * @author L
 */
@TableName(value = "cashier_salary")
@Data
public class CashierSalary extends BaseEntity {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 工资日期
     */
    private Date date;

    /**
     * 员工ID
     */
    private Integer userId;

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 工资金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付状态
     */
    private PayStatusEnum status;

    /**
     * 备注
     */
    private String remark;

}