package com.bootdo.core.factory;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.core.utils.HttpServletUtil;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 默认分页参数构建
 *
 * @author L
 */
public class PageFactory {

    private PageFactory() {

    }

    /**
     * 每页大小（默认20）
     */
    private static final String PAGE_SIZE_PARAM_NAME = "rows";

    /**
     * 第几页（从1开始）
     */
    private static final String PAGE_NO_PARAM_NAME = "page";

    /**
     * 排序字段
     */
    private static final String SORT_FIELD_PARAM_NAME = "sidx";

    /**
     * 排序方向
     */
    private static final String SORT_ORDER_PARAM_NAME = "sord";
    private static final String SORT_ORDER_PARAM_ASC = "asc";

    /**
     * 默认分页，在使用时PageFactory.defaultPage会自动获取pageSize和pageNo参数
     */
    public static <T> Page<T> defaultPage() {

        HttpServletRequest request = HttpServletUtil.getRequest();

        // 每页条数、第几页
        int pageSize = NumberUtil.parseInt(request.getParameter(PAGE_SIZE_PARAM_NAME), 20);
        int pageNo = NumberUtil.parseInt(request.getParameter(PAGE_NO_PARAM_NAME), 1);

        // 排序字段、方向
        String sortField = StrUtil.cleanBlank(request.getParameter(SORT_FIELD_PARAM_NAME));
        String sortOrder = StrUtil.cleanBlank(request.getParameter(SORT_ORDER_PARAM_NAME));

        Page<T> page = new Page<T>(pageNo, pageSize);
        // 设置排序
        if (ObjectUtil.isAllNotEmpty(sortField, sortOrder)) {
            String dbSortField = StrUtil.toUnderlineCase(sortField);
            page.addOrder(OrderItem.withExpression(dbSortField, SORT_ORDER_PARAM_ASC.equalsIgnoreCase(sortOrder)));
        }

        return page;
    }

    public static <T> Page<T> defalultAllPage() {
        return new Page<>(1, Integer.MAX_VALUE);
    }

}
