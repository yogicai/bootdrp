package com.bootdo.rp.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillSource;
import com.bootdo.common.enumeration.BillType;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 收付款单
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Data
public class RPOrderDO {

	private Integer id;

	/**
	 * 单据日期
	 */
	@Excel(name = "单据日期", exportFormat = "yyyy-MM-dd", width = 15)
	private Date billDate;

	/**
	 * 应收、应付票据编号
	 */
	@Excel(name = "应收、应付票据编号", width = 28)
	private String billNo;

	/**
	 * 单据类型
	 */
	@Excel(name = "单据类型", enumExportField = "remark")
	private BillType billType;

	/**
	 * 收付款机构ID
	 */
	@Excel(name = "收付款机构ID")
	private String debtorId;

	/**
	 * 收付款机构名称
	 */
	@Excel(name = "收付款机构名称")
	private String debtorName;

	/**
	 * 核销人员ID
	 */
	@Excel(name = "核销人员ID")
	private String checkId;

	/**
	 * 核销人员名称
	 */
	@Excel(name = "核销人员名称")
	private String checkName;

	/**
	 * 机构总欠款
	 */
	@Excel(name = "机构总欠款", numFormat = "#,##0.00", width = 15)
	private BigDecimal debtAmount;

	/**
	 * 本次结算金额
	 */
	@Excel(name = "本次结算金额", numFormat = "#,##0.00", width = 15)
	private BigDecimal paymentAmount;

	/**
	 * 本次核销金额
	 */
	@Excel(name = "本次核销金额", numFormat = "#,##0.00", width = 15)
	private BigDecimal checkAmount;

	/**
	 * 整单折扣
	 */
	@Excel(name = "整单折扣", numFormat = "#,##0.00", width = 15)
	private BigDecimal discountAmount;

	/**
	 * 审核状态
	 */
	@Excel(name = "审核状态", enumExportField = "remark")
	private AuditStatus auditStatus;

	/**
	 * 审核人ID
	 */
	@Excel(name = "审核人ID")
	private String auditId;

	/**
	 * 审核人名称
	 */
	@Excel(name = "审核人名称")
	private String auditName;

	/**
	 * 单据来源
	 */
	@Excel(name = "单据来源", enumExportField = "remark")
	private BillSource billSource;

	/**
	 * 备注
	 */
	@Excel(name = "备注", width = 20)
	private String remark;

	/**
	 * 创建时间
	 */
	@Excel(name = "创建时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Excel(name = "修改时间", exportFormat = "yyyy-MM-dd hh:mm:ss", width = 20)
	private Date updateTime;

	private final List<RPOrderEntryDO> entryDOList = Lists.newArrayList();
	private final List<RPOrderSettleDO> settleDOList = Lists.newArrayList();

}
