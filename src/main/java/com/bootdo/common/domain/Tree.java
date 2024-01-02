package com.bootdo.common.domain;

import cn.hutool.core.map.MapUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * tree TODO <br>
 *
 * @author kangxu2 2017-1-7
 */
@Data
public class Tree<T> {
    /**
     * 节点ID
     */
    private String id;
    /**
     * 显示节点文本
     */
    private String text;
    /**
     * 节点状态，open closed
     */
    private Map<String, Object> state;
    /**
     * 节点是否被选中 true false
     */
    private boolean checked = false;
    /**
     * 节点属性
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 节点的子节点
     */
    private List<Tree<T>> children = new ArrayList<>();

    /**
     * 父ID
     */
    private String parentId;
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;

    public String getType() {
        return MapUtil.getStr(attributes, "type");
    }


}