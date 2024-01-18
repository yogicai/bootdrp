package com.bootdo.core.pojo.response;

import lombok.Data;

import java.util.List;

/**
 * @author L
 */
@Data
public class PageR {
    // 总记录数
    private int total;
    // 列表数据
    private List<?> rows;

    /**
     * 分页
     */
    public PageR(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

}
