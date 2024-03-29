package com.bootdo.core.factory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.core.utils.HttpServletUtil;

import javax.servlet.http.HttpServletRequest;


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
     * 默认分页，在使用时PageFactory.defaultPage会自动获取pageSize和pageNo参数
     */
    public static <T> Page<T> defaultPage() {

        int pageSize = 20;
        int pageNo = 1;

        HttpServletRequest request = HttpServletUtil.getRequest();

        //每页条数
        String pageSizeString = request.getParameter(PAGE_SIZE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageSizeString)) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        //第几页
        String pageNoString = request.getParameter(PAGE_NO_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageNoString)) {
            pageNo = Integer.parseInt(pageNoString);
        }

        return new Page<>(pageNo, pageSize);
    }

    public static <T> Page<T> defalultAllPage() {
        return new Page<>(1, Integer.MAX_VALUE);
    }

}
