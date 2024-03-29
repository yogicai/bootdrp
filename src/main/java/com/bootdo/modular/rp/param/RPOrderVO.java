package com.bootdo.modular.rp.param;

import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class RPOrderVO {
    /**
     * 单据日期
     */
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date billDate;
    /**
     * 店铺编号
     */
    @NotBlank
    private String shopNo;
    /**
     * 单据编号
     */
    private String billNo;
    /**
     * 收款、付款标识
     */
    private BillType billType;
    /**
     * 买家ID
     */
    private String debtorId;
    /**
     * 买家名称
     */
    private String debtorName;
    /**
     * 收款人ID
     */
    private String checkId;
    /**
     * 收款人名称
     */
    private String checkName;
    /**
     * 整单折扣
     */
    private BigDecimal discountAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审核状态
     */
    private AuditStatus auditStatus;

    /**
     * 结算金额明细
     */
    private final List<RPOrderSettleVO> settleVOList = Lists.newArrayList();
    /**
     * 源单及核销金额明细
     */
    private final List<RPOrderEntryVO> entryVOList = Lists.newArrayList();

}
