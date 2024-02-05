package com.bootdo.modular.system.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.utils.BuildTree;
import com.bootdo.modular.system.dao.DeptDao;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.param.SysDeptParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class DeptService extends ServiceImpl<DeptDao, DeptDO> {

    public PageR page(SysDeptParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<DeptDO> list(SysDeptParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<DeptDO> pageList(Page<DeptDO> page, SysDeptParam param) {
        LambdaQueryWrapper<DeptDO> queryWrapper = Wrappers.lambdaQuery(DeptDO.class)
                .in(ObjectUtil.isNotEmpty(param.getName()), DeptDO::getName, StrUtil.split(param.getName(), StrUtil.COMMA))
                .in(ObjectUtil.isNotEmpty(param.getDeptId()), DeptDO::getDeptId, StrUtil.split(param.getDeptId(), StrUtil.COMMA))
                .ge(ObjectUtil.isNotEmpty(param.getStart()), DeptDO::getUpdateTime, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), DeptDO::getUpdateTime, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(DeptDO::getDeptId, param.getSearchText()).or().like(DeptDO::getName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = this.list().stream()
                .map(sysDept -> {
                    Tree<DeptDO> tree = new Tree<>();
                    tree.setId(sysDept.getDeptId().toString());
                    tree.setParentId(sysDept.getParentId().toString());
                    tree.setText(sysDept.getName());
                    tree.setState(MapUtil.of("opened", true));
                    return tree;
                }).collect(Collectors.toList());
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        return this.count(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getDeptId, deptId)) == 0;
    }

}
