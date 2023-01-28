package com.bootdo.po.controller.request;

import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author yogiCai
 * @date 2018-02-01 10:43:43
 */
@Data
public class OrderVO {
    /** 单据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    /** 单据编号 */
    private String billNo;
    /** 采购、退货标识 */
    private BillType billType;
    /** 供应商ID */
    private String vendorId;
    /** 供应商名称 */
    private String vendorName;
    /** 商品金额 */
    private BigDecimal entryAmountTotal;
    /** 合计金额 */
    private BigDecimal finalAmountTotal;
    /** 已付金额 */
    private BigDecimal paymentAmountTotal;
    /** 优惠率 */
    private BigDecimal discountRateTotal;
    /** 优惠金额 */
    private BigDecimal discountAmountTotal;
    /** 结算账号 */
    private String settleAccountTotal;
    /** 优惠金额 */
    private BigDecimal debtAccountTotal;
    /** 采购费用 */
    private BigDecimal purchaseFeeTotal;
    /** 备注 */
    private String remark;
    /** 审核状态 */
    private AuditStatus auditStatus;

    private final List<OrderEntryVO> entryVOList = Lists.newArrayList();

}
