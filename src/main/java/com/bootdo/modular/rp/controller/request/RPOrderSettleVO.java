package com.bootdo.modular.rp.controller.request;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author yogiCai
 * @date 2018-02-01 10:44:23
 */
@Data
public class RPOrderSettleVO {
    /**  */
    private Integer id;
    /** 商品ID */
    private String settleAccount;
    /** 收款金额 */
    private BigDecimal paymentAmount;
    /** 备注 */
    private String remark;

}
