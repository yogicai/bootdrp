package com.bootdo.core.pojo.request;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.utils.DateUtils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 *
 * @author L
 */
@Data
public class Query extends LinkedHashMap<String, Object> {

    private int offset;
    /**
     * 每页条数
     */
    private int limit;
    private String start;
    /**
     * 查询终止时间
     */
    private String end;

    public Query(Map<String, Object> params, boolean page) {
        this.putAll(params);
        this.start = DateUtils.getDayBegin(MapUtil.getStr(params, "start"));
        this.end = DateUtils.getDayEnd(MapUtil.getStr(params, "end"));
        // 分页参数
        this.offset = MapUtil.getInt(params, "offset", 0);
        this.limit = MapUtil.getInt(params, "limit", 10);

        this.put("start", start);
        this.put("end", end);

        if (page) {
            this.put("offset", offset);
            this.put("page", offset / limit + 1);
            this.put("limit", limit);
        }
    }

    public Query(Map<String, Object> params) {
        this(params, true);
    }

}
