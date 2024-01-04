package com.bootdo.modular.rp.controller.request;

import com.bootdo.core.enums.BillType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yogiCai
 * @date 2018-02-01 10:44:23
 */
@Data
public class RPOrderEntryVO {
    /**  */
    private Integer id;
    /** 源单编号 */
    private String srcBillNo;
    /** 源单类型 */
    private BillType srcBillType;
    /** 源单据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date srcBillDate;
    /** 单据金额 */
    private BigDecimal srcTotalAmount;
    /** 已核销金额 */
    private BigDecimal srcPaymentAmount;
    /** 本次核销金额 */
    private BigDecimal checkAmount;

}
