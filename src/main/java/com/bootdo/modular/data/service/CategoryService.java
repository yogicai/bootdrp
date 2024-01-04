package com.bootdo.modular.data.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.pojo.node.AsyncTree;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.utils.BuildTree;
import com.bootdo.modular.data.dao.CategoryDao;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.domain.ProductDO;
import com.google.common.collect.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author L
 */
@Service
public class CategoryService {
    @Resource
    private CategoryDao categoryDao;
    @Resource
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
        List<CategoryDO> categoryDOList = categoryDao.list(params);
        for (CategoryDO categoryDO : categoryDOList) {
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

    public List<AsyncTree<CategoryDO>> getAsyncTree(Map<String, Object> params) {
        List<AsyncTree<CategoryDO>> trees = new ArrayList<>();
        List<CategoryDO> categoryDOList = categoryDao.list(params);
        for (CategoryDO categoryDO : categoryDOList) {
            AsyncTree<CategoryDO> tree = new AsyncTree<>();
            tree.setId(categoryDO.getCategoryId().toString());
            tree.setParentId(categoryDO.getParentId().toString());
            tree.setText(categoryDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.buildAsync(trees);
    }

    public List<AsyncTree<CategoryDO>> getAsyncTreeLeaf(Map<String, Object> params) {
        List<AsyncTree<CategoryDO>> nodeList = Lists.newArrayList();
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

    public Map<String, List<Tree<Object>>> listTreeData(Map<String, Object> params) {
        List<Tree<Object>> trees = new ArrayList<>();
        List<Map<String, Object>> categoryData = categoryDao.listTreeData(params);

        Set<String> categorySet = Sets.newHashSet();
        for (Map<String, Object> map : categoryData) {
            Tree<Object> tree = new Tree<>();
            if (categorySet.add(MapUtil.getStr(map, "categoryId"))) {
                tree.setId(MapUtil.getStr(map, "categoryId"));
                tree.setParentId("0");
                tree.setText(MapUtil.getStr(map, "name"));
                Map<String, Object> attributes = new HashMap<>(16);
                attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
                tree.setAttributes(attributes);
                trees.add(tree);
            }
            tree = new Tree<>();
            tree.setId(MapUtil.getStr(map, "dataId"));
            tree.setParentId(MapUtil.getStr(map, "categoryId"));
            tree.setText(MapUtil.getStr(map, "dataName"));
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("type", MapUtil.getStr(map, "type") + "_DATA");
            tree.setAttributes(attributes);
            trees.add(tree);
        }

        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Object>> treeList = BuildTree.buildList(trees, ImmutableSet.of("0"));
        Map<String, List<Tree<Object>>> listTree = Maps.newHashMap();
        for (Tree<Object> node : treeList) {
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
