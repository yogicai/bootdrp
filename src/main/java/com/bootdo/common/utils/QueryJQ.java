package com.bootdo.common.utils;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 * @author yogiCai
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryJQ extends LinkedHashMap<String, Object> {

    /** 查询启始行 */
    private int offset;
    /** 查询条数 */
    private int limit;
    /** 当前页码 */
    private int page;
    /** 查询开始时间 */
    private String start;
    /** 查询终止时间 */
    private String end;

    public QueryJQ(Map<String, Object> params, boolean page) {
        try {
            this.putAll(params);
            this.start = DateUtils.getDayBegin(MapUtils.getString(params, "start"));
            this.end = DateUtils.getDayEnd(MapUtils.getString(params, "end"));
            this.limit = MapUtils.getIntValue(params, "rows");
            this.page = MapUtils.getIntValue(params, "page");
            this.offset = (this.page - 1) * this.limit;

            this.put("start", start);
            this.put("end", end);

            if (page) {
                this.put("offset", offset);
                this.put("limit", limit);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QueryJQ(Map<String, Object> params) {
        this(params, true);
    }
}

