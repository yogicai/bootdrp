package com.bootdo.modular.data.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.utils.BuildTree;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.data.dao.CategoryDao;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.param.CategoryQryParam;
import com.bootdo.modular.data.result.CategoryDataResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class CategoryService extends ServiceImpl<CategoryDao, CategoryDO> {
    @Resource
    private CategoryDao categoryDao;


    public Page<CategoryDO> pageList(Page<CategoryDO> page, CategoryQryParam param) {
        LambdaQueryWrapper<CategoryDO> queryWrapper = Wrappers.lambdaQuery(CategoryDO.class)
                .in(ObjectUtil.isNotEmpty(param.getType()), CategoryDO::getType, StrUtil.split(param.getType(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getStatus()), CategoryDO::getStatus, StrUtil.split(param.getStatus(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), CategoryDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), CategoryDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(CategoryDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public Tree<CategoryDO> getTree(CategoryQryParam param) {
        List<CategoryDO> categoryDOList = this.pageList(PageFactory.defalultAllPage(), param).getRecords();
        List<Tree<CategoryDO>> treeNodeList = categoryDOList.stream().map(category -> {
            Tree<CategoryDO> treeNode = new Tree<>();
            treeNode.setId(category.getCategoryId().toString());
            treeNode.setParentId(category.getParentId().toString());
            treeNode.setText(category.getName());
            treeNode.setState(MapUtil.of("opened", true));
            treeNode.setAttributes(MapUtil.of("type", category.getType()));
            return treeNode;
        }).collect(Collectors.toList());

        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(treeNodeList);
    }

    /**
     * 缓存_类目树
     */
    public Map<String, List<Tree<CategoryDO>>> listTree(CategoryQryParam param) {
        Tree<CategoryDO> trees = getTree(param);
        return trees.getChildren().stream().collect(Collectors.groupingBy(Tree::getType, Collectors.toList()));
    }

    /**
     * 缓存_类目数据
     */
    public Map<String, List<Tree<Object>>> listTreeData(Map<String, Object> params) {
        //所有类目及类目数据
        List<CategoryDataResult> categoryData = categoryDao.listTreeData(params);
        //类目节点
        List<Tree<Object>> treeNodeList = categoryData.stream()
                .filter(PoiUtil.distinctByKey(CategoryDataResult::getCategoryId)).map(data -> {
                    Tree<Object> treeNode = new Tree<>();
                    treeNode.setId(data.getCategoryId());
                    treeNode.setParentId("0");
                    treeNode.setText(data.getName());
                    treeNode.setAttributes(MapUtil.of("type", data.getType() + "_DATA"));
                    return treeNode;
                }).collect(Collectors.toList());
        //类目数据
        treeNodeList.addAll(categoryData.stream().map(data -> {
            Tree<Object> treeNode = new Tree<>();
            treeNode.setId(data.getDataId());
            treeNode.setParentId(data.getCategoryId());
            treeNode.setText(data.getDataName());
            treeNode.setAttributes(MapUtil.of("type", data.getType() + "_DATA"));
            return treeNode;
        }).collect(Collectors.toList()));

        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Object>> treeList = BuildTree.buildList(treeNodeList, CollUtil.newHashSet("0"));
        return treeList.stream().collect(Collectors.groupingBy(Tree::getType, Collectors.toList()));
    }

    public Map<String, List<CategoryDO>> lists() {

        return this.list().stream().collect(Collectors.groupingBy(CategoryDO::getType, Collectors.toList()));
    }

}
