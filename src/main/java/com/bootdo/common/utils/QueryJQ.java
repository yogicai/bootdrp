package com.bootdo.common.utils;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
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
    /** 排序列 */
    private String sort;
    /** 升序、降序 */
    private String order;

    public QueryJQ(Map<String, Object> params, boolean page) {
        try {
            this.putAll(params);
            this.start = DateUtils.getDayBegin(MapUtil.getStr(params, "start"));
            this.end = DateUtils.getDayEnd(MapUtil.getStr(params, "end"));

            this.sort = MapUtil.getStr(params, "sidx");
            this.order = MapUtil.getStr(params, "sord");

            this.put("sort", StrUtil.toUnderlineCase(this.sort));
            this.put("order", this.order);

            this.put("start", start);
            this.put("end", end);

            if (page) {
                this.limit = MapUtil.getInt(params, "rows");
                this.page = MapUtil.getInt(params, "page");
                this.offset = (this.page - 1) * this.limit;

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

