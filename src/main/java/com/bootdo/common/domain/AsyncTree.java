package com.bootdo.common.domain;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @date 2018-03-17 19:35:03
 */
public class AsyncTree<T> {

	private String id;
	private String text;
    private String parentId;
	private Object children;
    private boolean isLeaf;
    private Map<String, Object> state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public AsyncTree(String id, String text, String parentId) {
        super();
        this.id = id;
        this.text = text;
        this.parentId = parentId;
    }

    public AsyncTree(String id, String text, Map<String, Object> state, List<AsyncTree<T>> children) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
		this.children = children;
	}

	public AsyncTree() {
		super();
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}