package com.bootdo.cashier.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 日记账
 *
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
public class JournalDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//记账日期
	private Date date;
	//摘要
	private String digest;
	//收入
	private BigDecimal debit;
	//支出
	private BigDecimal credit;
	//余额
	private BigDecimal balance;
    //账户
    private String account;
	//备注
	private String remark;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：记账日期
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * 获取：记账日期
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * 设置：摘要
	 */
	public void setDigest(String digest) {
		this.digest = digest;
	}
	/**
	 * 获取：摘要
	 */
	public String getDigest() {
		return digest;
	}
	/**
	 * 设置：收入
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	/**
	 * 获取：收入
	 */
	public BigDecimal getDebit() {
		return debit;
	}
	/**
	 * 设置：支出
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	/**
	 * 获取：支出
	 */
	public BigDecimal getCredit() {
		return credit;
	}
	/**
	 * 设置：余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	/**
	 * 获取：余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}
    /**
     * 设置：账户
     */
    public void setAccount(String account) {
        this.account = account;
    }
    /**
     * 获取：账户
     */
    public String getAccount() {
        return account;
    }
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
