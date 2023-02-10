package com.bootdo.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yogiCai
 */
@Data
public class PageJQUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 总页数 */
	private int total;
	/** 当前页 */
	private int page;
	/** 总记录数 */
	private int records;
	/** 列表数据 */
	private List<?> rows;
	/** 总合计 */
	private int totalAmount;
	/** 总数量合计 */
	private Map<String, Object> extra = new HashMap<>(3);


	/**
	 * @param list    当前页数据结果集
	 * @param total   总页数
	 * @param page    当前页页码
	 * @param records 总行数
	 */
	public PageJQUtils(List<?> list, int total, int page, int records) {
		this.rows = list;
		this.total = total;
		this.page = page;
		this.records = records;
	}

	public PageJQUtils(List<?> list, int total, int page, int records, int totalAmount) {
		this.rows = list;
		this.total = total;
		this.page = page;
		this.records = records;
		this.totalAmount = totalAmount;
	}

	public PageJQUtils(List<?> list, int total, int page, int records, Map<String, Object> extra) {
		this.rows = list;
		this.total = total;
		this.page = page;
		this.records = records;
		this.extra = extra;
	}
}
