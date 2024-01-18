package com.bootdo.modular.data.domain;

import lombok.Data;

import java.io.Serializable;


/**
 * 类别管理
 *
 * @author yogiCai
 * @date 2018-02-04 17:12:20
 */
@Data
public class CategoryDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
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
