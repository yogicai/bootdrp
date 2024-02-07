package com.bootdo.modular.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.system.dao.RoleDao;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.domain.RoleMenuDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author L
 */
@Service
public class RoleService extends ServiceImpl<RoleDao, RoleDO> {
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserRoleService userRoleService;


    public List<RoleDO> list(Long userId) {
        List<Long> rolesIds = userRoleService.listRoleId(userId);
        List<RoleDO> roles = this.list(Wrappers.query());
        for (RoleDO roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), roleId)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }

    @Transactional
    public void saveRole(RoleDO role) {
        this.save(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuService.removeByRoleId(roleId);
        roleMenuService.saveBatch(rms);
    }

    @Transactional
    public void removeRole(Long id) {
        this.removeById(id);
        roleMenuService.removeByRoleId(id);
    }

    @Transactional
    public void removeBatchRole(List<Long> ids) {
        ids.forEach(this::removeRole);
    }

    @Transactional
    public void updateRole(RoleDO role) {
        this.updateById(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuService.removeByRoleId(roleId);
        roleMenuService.saveBatch(rms);
    }

}
