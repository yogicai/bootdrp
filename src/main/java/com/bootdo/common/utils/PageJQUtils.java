package com.bootdo.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yogiCai
 */
public class PageJQUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	// 总页数
	private int total;
    // 当前页
    private int page;
    // 总记录数
    private int records;
	// 列表数据
	private List<?> rows;


    /**
     * @param list 当前页数据结果集
     * @param total 总页数
     * @param page 当前页页码
     * @param records 总行数
     */
	public PageJQUtils(List<?> list, int total, int page, int records) {
		this.rows = list;
		this.total = total;
		this.page = page;
		this.records = records;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
}
