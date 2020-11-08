package com.bootdo.common.utils;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 * @Author: yogiCai
 */
public class QueryJQ extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;

    // 查询启始行
    private int offset;
    // 查询条数
    private int limit;
    // 当前页码
    private int page;
    // 查询开始时间
    private String start;
    // 查询终止时间
    private String end;

	public QueryJQ(Map<String, Object> params){
        try {
            this.putAll(params);
            this.start = DateUtils.getDayBegin(MapUtils.getString(params, "start"));
            this.end = DateUtils.getDayEnd(MapUtils.getString(params, "end"));
            this.limit = MapUtils.getIntValue(params, "rows");
            this.page = MapUtils.getIntValue(params, "page");
            this.offset = (this.page - 1) * this.limit;

            this.put("start", start);
            this.put("end", end);
            this.put("offset", offset);
            this.put("offset", offset);
            this.put("limit", limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
