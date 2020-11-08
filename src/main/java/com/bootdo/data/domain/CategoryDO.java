package com.bootdo.data.domain;

import java.io.Serializable;



/**
 * 类别管理
 * @Author: yogiCai
 * @date 2018-02-04 17:12:20
 */
public class CategoryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long categoryId;
	//上级ID，顶级为0
	private Long parentId;
	//名称
	private String name;
	//排序
	private Integer orderNum;
	//分类
	private String type;
	//状态
	private Integer status = 0;

	/**
	 * 设置：
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * 获取：
	 */
	public Long getCategoryId() {
		return categoryId;
	}
	/**
	 * 设置：上级ID，顶级为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级ID，顶级为0
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：分类
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：分类
	 */
	public String getType() {
		return type;
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
}
