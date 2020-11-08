package com.bootdo.common.utils;

import org.apache.commons.collections.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 
	private int offset;
	// 每页条数
	private int limit;
    private String start;
    // 查询终止时间
    private String end;

	public Query(Map<String, Object> params) {
		this.putAll(params);
        this.start = DateUtils.getDayBegin(com.bootdo.common.utils.MapUtils.getString(params, "start"));
        this.end = DateUtils.getDayEnd(com.bootdo.common.utils.MapUtils.getString(params, "end"));
		this.offset = MapUtils.getIntValue(params, "offset"); // 分页参数
		this.limit = MapUtils.getIntValue(params, "limit", 10);

        this.put("start", start);
        this.put("end", end);
		this.put("offset", offset);
		this.put("page", offset / limit + 1);
		this.put("limit", limit);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
