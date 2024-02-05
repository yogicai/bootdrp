package com.bootdo.modular.rp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @since 2018-02-21 21:23:27
 */
@FieldNameConstants
@TableName(value = "rp_order_entry")
@Data
public class RPOrderEntryDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 应收、应付票据编号
     */
    private String billNo;
    /**
     * 单据日期
     */
    private Date srcBillDate;
    /**
     * 单据类型
     */
    private BillType srcBillType;
    /**
     * 单据编号
     */
    private String srcBillNo;
    /**
     * 总欠款
     */
    private BigDecimal srcTotalAmount;
    /**
     * 已付金额
     */
    private BigDecimal srcPaymentAmount;
    /**
     * 本次核销金额
     */
    private BigDecimal checkAmount;

}
