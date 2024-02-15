package com.bootdo.modular.data.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;


/**
 * 类别管理
 *
 * @author yogiCai
 * @since 2018-02-04 17:12:20
 */
@TableName(value = "data_category")
@Data
public class CategoryDO extends BaseEntity {

    /**
     *
     */
    @TableId
    private Long categoryId;
    /**
     * 上级ID，顶级为0
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 分类
     */
    private String type;
    /**
     * 状态
     */
    private Integer status = 0;

}
