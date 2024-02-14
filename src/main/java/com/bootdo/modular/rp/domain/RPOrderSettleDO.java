package com.bootdo.modular.rp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;


/**
 * 应收、应付票据结算表
 *
 * @author yogiCai
 * @since 2018-02-21 21:23:27
 */
@FieldNameConstants
@TableName(value = "rp_order_settle")
@Data
public class RPOrderSettleDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 店铺编号
     */
    private String shopNo;
    /**
     * 应收、应付票据编号
     */
    private String billNo;
    /**
     * 结算账户
     */
    private String settleAccount;
    /**
     * 结算账户名称
     */
    private String settleName;
    /**
     * 已付金额
     */
    private BigDecimal paymentAmount;
    /**
     * 备注
     */
    private String remark;

}
