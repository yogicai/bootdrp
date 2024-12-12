package com.bootdo.core.pojo.base.param;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bootdo.config.converter.JsonListFormat;
import lombok.Data;

import javax.validation.groups.Default;
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
     * 店铺权限
     */
    @JsonListFormat
    private List<Long> shopNo;

    /**
     * 开始时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 排序列（jqGrid）
     */
    private String sidx;

    /**
     * 升序、降序（jqGrid）：desc asc
     */
    private String sord;

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
     * （jqGrid）列表是否升序：默认降序
     */
    public boolean isAsc() {
        return "desc".equals(this.sord);
    }

    /**
     * 参数校验分组：分页
     */
    public interface page extends Default {
    }

    /**
     * 参数校验分组：列表
     */
    public interface list extends Default {
    }

    /**
     * 参数校验分组：增加
     */
    public interface add extends Default {
    }

    /**
     * 参数校验分组：编辑
     */
    public interface edit extends Default {
    }

    /**
     * 参数校验分组：删除
     */
    public interface delete extends Default {
    }

    /**
     * 参数校验分组：详情
     */
    public interface detail extends Default {
    }

}
