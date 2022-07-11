package com.bootdo.rp.domain;

import com.bootdo.common.enumeration.BillType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 应收、应付票据核销目标单据
 * 
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Data
public class RPOrderEntryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//应收、应付票据编号
	private String billNo;
	//单据日期
	private Date srcBillDate;
	//单据类型
	private BillType srcBillType;
	//单据编号
	private String srcBillNo;
	//总欠款
	private BigDecimal srcTotalAmount;
	//已付金额
	private BigDecimal srcPaymentAmount;
	//本次核销金额
	private BigDecimal checkAmount;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

}
