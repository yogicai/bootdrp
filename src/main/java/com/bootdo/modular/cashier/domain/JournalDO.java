package com.bootdo.modular.cashier.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 日记账
 *
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
@Data
public class JournalDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 记账日期 */
	private Date date;
	/** 摘要 */
	private String digest;
	/** 收入 */
	private BigDecimal debit;
	/** 支出 */
	private BigDecimal credit;
	/** 余额 */
	private BigDecimal balance;
    /** 账户 */
    private String account;
	/** 备注 */
	private String remark;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;

}
