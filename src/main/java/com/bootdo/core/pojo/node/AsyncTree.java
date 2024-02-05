package com.bootdo.core.pojo.node;

import lombok.Data;

import java.util.Map;

/**
 * @author yogiCai
 * @since 2018-03-17 19:35:03
 */
@Data
public class AsyncTree<T> {

    private String id;
    private String text;
    private String parentId;
    private Object children;
    private boolean isLeaf;
    private Map<String, Object> state;

}