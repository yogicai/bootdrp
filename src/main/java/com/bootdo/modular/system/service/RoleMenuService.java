package com.bootdo.modular.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.system.dao.RoleMenuDao;
import com.bootdo.modular.system.domain.RoleMenuDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author L
 * @since 2024-02-06 16:29
 */
@Service
public class RoleMenuService extends ServiceImpl<RoleMenuDao, RoleMenuDO> {

    public void removeByRoleId(Long roleId) {
        this.remove(Wrappers.lambdaQuery(RoleMenuDO.class).eq(RoleMenuDO::getRoleId, roleId));
    }

    public List<Long> listMenuIdByRoleId(Long roleId) {
        return this.list(Wrappers.lambdaQuery(RoleMenuDO.class).eq(RoleMenuDO::getRoleId, roleId)).stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
    }

}
