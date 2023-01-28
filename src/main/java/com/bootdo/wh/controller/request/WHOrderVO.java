package com.bootdo.wh.controller.request;

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
* @data 2018-02-01 10:43:43
*/
@Data
public class WHOrderVO {

    /** 单据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    /** 单据编号 */
    private String billNo;
    /** 出入库单据类型 */
    private BillType billType;
    /** 业务类型 */
    private String serviceType;
    /** 相关机构ID */
    private String debtorId;
    /** 相关机构名称 */
    private String debtorName;
    /** 审核人ID */
    private String auditId;
    /** 审核人名称 */
    private String auditName;
    /** 数量 */
    private BigDecimal totalQty;
    /** 金额 */
    private BigDecimal entryAmount;
    /** 审核状态 */
    private AuditStatus auditStatus;
    /** 备注 */
    private String remark;

    /** 出入库商品明细 */
    private final List<WHOrderEntryVO> entryVOList = Lists.newArrayList();

}
