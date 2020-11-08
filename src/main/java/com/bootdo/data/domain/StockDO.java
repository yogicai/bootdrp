package com.bootdo.data.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 仓库表
 * @author yogiCai
 * @date 2018-02-18 16:23:32
 */
public class StockDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//仓库编号
	private String stockNo;
	//仓库名称
	private String stockName;
	//仓库地址
	private String stockAddress;
	//状态
	private Integer status = 0;
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
	 * 设置：仓库编号
	 */
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	/**
	 * 获取：仓库编号
	 */
	public String getStockNo() {
		return stockNo;
	}
	/**
	 * 设置：仓库名称
	 */
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	/**
	 * 获取：仓库名称
	 */
	public String getStockName() {
		return stockName;
	}
	/**
	 * 设置：仓库地址
	 */
	public void setStockAddress(String stockAddress) {
		this.stockAddress = stockAddress;
	}
	/**
	 * 获取：仓库地址
	 */
	public String getStockAddress() {
		return stockAddress;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
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
