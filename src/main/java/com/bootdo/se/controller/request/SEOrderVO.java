package com.bootdo.se.controller.request;

import com.bootdo.common.enumeration.AuditStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
@Data
public class SEOrderVO {
    //单据日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    //单据编号
    private String billNo;
    //销售人员ID
    private String billerId;
    //仓库编号
    private String stockNo;
    //客户ID
    private String consumerId;
    //客户名称
    private String consumerName;
    //商品金额
    private BigDecimal entryAmountTotal;
    //合计金额
    private BigDecimal finalAmountTotal;
    //已付金额
    private BigDecimal paymentAmountTotal;
    //优惠率
    private BigDecimal discountRateTotal;
    //优惠金额
    private BigDecimal discountAmountTotal;
    //结算账号
    private String settleAccountTotal;
    //优惠金额
    private BigDecimal debtAccountTotal;
    //客户承担费用
    private BigDecimal expenseFeeTotal;
    //采购费用
    private BigDecimal purchaseFeeTotal;
    //备注
    private String remark;
    //审核状态
    private AuditStatus auditStatus;

    private final List<SEOrderEntryVO> entryVOList = Lists.newArrayList();

}
