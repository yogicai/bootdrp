package com.bootdo.rp.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 应收、应付票据结算表
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Data
public class RPOrderSettleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;
    /** 应收、应付票据编号 */
    private String billNo;
    /** 结算账户 */
    private String settleAccount;
    /** 结算账户名称 */
    private String settleName;
    /** 已付金额 */
    private BigDecimal paymentAmount;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;

}
