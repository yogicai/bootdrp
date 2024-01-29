package com.bootdo.core.pojo.base.param;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 通用基础参数，相关实体参数校验可继承此类
 *
 * @author L
 * @since 2024-01-19 11:32
 */
@Data
public class BaseParam {

    /**
     * 搜索值
     */
    private String searchText;

    /**
     * 数据权限
     */
    private List<Long> dataScope;

    /**
     * 店铺权限
     */
    private List<Long> shopScope;

    /**
     * 开始时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 时间区间查询时，结束时间转换：yyyy-MM-dd => yyyy-MM-dd 23:59:59
     */
    public Date getEnd() {
        if (ObjectUtil.isNotNull(this.end)) {
            return DateUtil.beginOfDay(this.end).equals(this.end) ? DateUtil.endOfDay(this.end) : this.end;
        }
        return null;
    }

    /**
     * 参数校验分组：分页
     */
    public @interface page {
    }

    /**
     * 参数校验分组：列表
     */
    public @interface list {
    }

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }

}
