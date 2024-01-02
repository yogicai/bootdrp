package com.bootdo.data.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.domain.AsyncTree;
import com.bootdo.common.domain.Tree;
import com.bootdo.common.utils.BuildTree;
import com.bootdo.data.dao.CategoryDao;
import com.bootdo.data.dao.ProductDao;
import com.bootdo.data.domain.CategoryDO;
import com.bootdo.data.domain.ProductDO;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ProductDao productDao;

    public CategoryDO get(Long categoryId) {
        return categoryDao.get(categoryId);
    }

    public List<CategoryDO> list(Map<String, Object> map) {
        return categoryDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return categoryDao.count(map);
    }

    public int save(CategoryDO category) {
        return categoryDao.save(category);
    }

    public int update(CategoryDO category) {
        return categoryDao.update(category);
    }

    public int remove(Long categoryId) {
        return categoryDao.remove(categoryId);
    }

    public int batchRemove(Long[] categoryIds) {
        return categoryDao.batchRemove(categoryIds);
    }

    public Tree<CategoryDO> getTree(Map<String, Object> params) {
        List<Tree<CategoryDO>> trees = new ArrayList<Tree<CategoryDO>>();
        List<CategoryDO> categoryDOS = categoryDao.list(params);
        for (CategoryDO categoryDO : categoryDOS) {
            Tree<CategoryDO> tree = new Tree<CategoryDO>();
            tree.setId(categoryDO.getCategoryId().toString());
            tree.setParentId(categoryDO.getParentId().toString());
            tree.setText(categoryDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("type", categoryDO.getType());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<CategoryDO> t = BuildTree.build(trees);
        return t;
    }

    public List<AsyncTree> getAsyncTree(Map<String, Object> params) {
        List<AsyncTree> trees = new ArrayList<>();
        List<CategoryDO> categoryDOS = categoryDao.list(params);
        for (CategoryDO categoryDO : categoryDOS) {
            AsyncTree<CategoryDO> tree = new AsyncTree<>();
            tree.setId(categoryDO.getCategoryId().toString());
            tree.setParentId(categoryDO.getParentId().toString());
            tree.setText(categoryDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.buildAsync(trees);
    }

    public List<AsyncTree> getAsyncTreeLeaf(Map<String, Object> params) {
        List<AsyncTree> nodeList = Lists.newArrayList();
        List<ProductDO> productDOList = productDao.list(ImmutableMap.of("type", MapUtil.getStr(params, "id")));
        for (ProductDO productDO : productDOList) {
            AsyncTree<CategoryDO> tree = new AsyncTree<>();
            tree.setId(productDO.getNo().toString());
            tree.setText(productDO.getName());
            tree.setParentId(MapUtil.getStr(params, "id"));
            tree.setLeaf(true);
            nodeList.add(tree);
        }
        return nodeList;
    }

    public Map<String, List<Tree<CategoryDO>>> listTree(Map<String, Object> params) {
        Tree<CategoryDO> trees = getTree(params);
        Map<String, List<Tree<CategoryDO>>> listTree = Maps.newHashMap();
        for (Tree<CategoryDO> node : trees.getChildren()) {
            String type = MapUtil.getStr(node.getAttributes(), "type");
            if (!listTree.containsKey(type)) {
                listTree.put(type, new ArrayList<>());
            }
            listTree.get(type).add(node);
        }
        return listTree;
    }

    public Map<String, List<Tree<Map>>> listTreeData(Map<String, Object> params) {
        List<Tree<Map>> trees = new ArrayList<Tree<Map>>();
        List<Map> categoryData = categoryDao.listTreeData(params);

        Set<String> categorySet = Sets.newHashSet();
        for (Map map : categoryData) {
            Tree<Map> tree = new Tree<Map>();
            if (categorySet.add(MapUtil.getStr(map, "categoryId"))) {
                tree.setId(MapUtil.getStr(map, "categoryId"));
                tree.setParentId("0");
                tree.setText(MapUtil.getStr(map, "name"));
                Map<String, Object> attributes = new HashMap<>(16);
                attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
                tree.setAttributes(attributes);
                trees.add(tree);
            }
            tree = new Tree<Map>();
            tree.setId(MapUtil.getStr(map, "dataId"));
            tree.setParentId(MapUtil.getStr(map, "categoryId"));
            tree.setText(MapUtil.getStr(map, "dataName"));
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
            tree.setAttributes(attributes);
            trees.add(tree);
        }

        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Map>> treeList = BuildTree.buildList(trees, ImmutableSet.of("0"));
        Map<String, List<Tree<Map>>> listTree = Maps.newHashMap();
        for (Tree<Map> node : treeList) {
            String type = MapUtil.getStr(node.getAttributes(), "type");
            if (!listTree.containsKey(type)) {
                listTree.put(type, new ArrayList<>());
            }
            listTree.get(type).add(node);
        }
        return listTree;
    }


    public Map<String, List<CategoryDO>> lists(Map<String, Object> map) {
        Map<String, List<CategoryDO>> maps = new HashMap<>();
        List<CategoryDO> list = categoryDao.list(map);
        for (CategoryDO categoryDO : list) {
            if (!maps.containsKey(categoryDO.getType())) {
                maps.put(categoryDO.getType(), new ArrayList<>());
            }
            maps.get(categoryDO.getType()).add(categoryDO);
        }
        return maps;
    }

}
