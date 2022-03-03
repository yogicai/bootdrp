package com.bootdo.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.bootdo.common.domain.AsyncTree;
import com.bootdo.common.domain.Tree;

import java.util.*;

public class BuildTree {

	public static <T> Tree<T> build(List<Tree<T>> nodes) {

		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

		for (Tree<T> children : nodes) {

			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);

				continue;
			}

			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					continue;
				}
			}

		}

		Tree<T> root = new Tree<T>();
		if (topNodes.size() == 1) {
			root = topNodes.get(0);
		} else {
			root.setId("-1");
			root.setParentId("");
			root.setHasParent(false);
			root.setChildren(true);
			root.setChecked(true);
			root.setChildren(topNodes);
			root.setText("顶级节点");
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			root.setState(state);
		}

		return root;
	}

    public static List<AsyncTree> buildAsync(List<AsyncTree> nodes) {
        if (nodes == null) return null;
        List<AsyncTree> topNodes = new ArrayList<>();
        for (AsyncTree parent : nodes) {
            String pid = parent.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(parent);
            }
            boolean leafNode = true;
            String id = parent.getId();
            List<AsyncTree> childList = new ArrayList<>();
            for (AsyncTree children : nodes) {
                String cPid = children.getParentId();
                if (id != null && id.equals(cPid)) {
                    childList.add(children);
                    leafNode = false;
                }
            }
            if (leafNode) {
                parent.setChildren(true);
            } else {
                parent.setChildren(childList);
            }
        }
        return topNodes;
    }

	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

		for (Tree<T> children : nodes) {

			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);

				continue;
			}

			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);

					continue;
				}
			}

		}

		CollectionUtil.sort(topNodes, Comparator.comparing(tree -> NumberUtils.toInt(tree.getId())));
		topNodes.stream().forEach(tree -> tree.getChildren().sort(Comparator.comparing(t -> NumberUtils.toInt(t.getId()))));

		return topNodes;
	}

    public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, Set<String> topNode) {
        if (nodes == null) { return null;  }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
        for (Tree<T> node : nodes) {
            String pid = node.getParentId();
            if (pid == null || topNode.contains(pid)) {
                topNodes.add(node);
            }
        }
        for (Tree<T> parent : topNodes) {
            String id = parent.getId();
            for(Tree<T> children : nodes) {
                String pid = children.getParentId();
                if (pid != null && pid.equals(id) && parent.getType().equals(children.getType())) {
                    parent.getChildren().add(children);
                    parent.setChildren(true);
                    children.setHasParent(true);
                }
            }
        }
        return topNodes;
    }
}