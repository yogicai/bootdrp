package com.bootdo.core.pojo.response;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public PageR(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public <T> PageR(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotal(PageUtil.totalPage(Convert.toInt(page.getTotal()), Convert.toInt(page.getSize())));
    }
}
