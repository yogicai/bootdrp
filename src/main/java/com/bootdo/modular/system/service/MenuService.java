package com.bootdo.modular.system.service;

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
import com.bootdo.modular.system.dao.MenuDao;
import com.bootdo.modular.system.domain.MenuDO;
import com.bootdo.modular.system.param.SysMenuParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author L
 */
@Service
public class MenuService extends ServiceImpl<MenuDao, MenuDO> {
    @Resource
    private RoleMenuService roleMenuService;


    public PageR page(SysMenuParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<MenuDO> list(SysMenuParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<MenuDO> pageList(Page<MenuDO> page, SysMenuParam param) {
        LambdaQueryWrapper<MenuDO> queryWrapper = Wrappers.lambdaQuery(MenuDO.class)
                .ge(ObjectUtil.isNotEmpty(param.getStart()), MenuDO::getGmtCreate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), MenuDO::getGmtCreate, param.getEnd());

        return this.page(page, queryWrapper);
    }


    public Tree<MenuDO> getTree() {
        List<Tree<MenuDO>> trees = new ArrayList<>();
        List<MenuDO> menuDOs = this.list(Wrappers.query());
        for (MenuDO sysMenuDO : menuDOs) {
            Tree<MenuDO> tree = new Tree<>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    public Tree<MenuDO> getTree(Long id) {
        // 根据roleId查询权限
        List<MenuDO> menus = this.list(Wrappers.query());
        List<Long> menuIds = roleMenuService.listMenuIdByRoleId(id);
        List<Long> temp = menuIds;
        for (MenuDO menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<Tree<MenuDO>> trees = new ArrayList<>();
        List<MenuDO> menuDOs = this.list(Wrappers.query());
        for (MenuDO sysMenuDO : menuDOs) {
            Tree<MenuDO> tree = new Tree<>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Long menuId = sysMenuDO.getMenuId();
            tree.setState(MapUtil.of("selected", menuIds.contains(menuId)));
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    public Set<String> listPerms(Long userId) {
        return this.baseMapper.listUserPerms(userId)
                .stream()
                .filter(StrUtil::isNotBlank)
                .map(perm -> StrUtil.split(perm.trim(), StrUtil.COMMA))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public List<Tree<MenuDO>> listMenuTree(Long id) {
        List<Tree<MenuDO>> trees = new ArrayList<>();
        List<MenuDO> menuDOs = this.baseMapper.listMenuByUserId(id);
        for (MenuDO sysMenuDO : menuDOs) {
            Tree<MenuDO> tree = new Tree<>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysMenuDO.getUrl());
            attributes.put("icon", sysMenuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.buildList(trees, "0");
    }

}
