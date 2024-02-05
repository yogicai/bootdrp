package com.bootdo.modular.system.service.impl;

import com.bootdo.modular.system.dao.RoleDao;
import com.bootdo.modular.system.dao.RoleMenuDao;
import com.bootdo.modular.system.dao.UserRoleDao;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.domain.RoleMenuDO;
import com.bootdo.modular.system.service.RoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author L
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    RoleDao roleDao;
    @Resource
    RoleMenuDao roleMenuDao;
    @Resource
    UserRoleDao userRoleDao;

    public static final String ROLE_ALL_KEY = "\"role_all\"";
    public static final String DEMO_CACHE_NAME = "role";


    @Cacheable(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Override
    public List<RoleDO> list() {
        return roleDao.list(new HashMap<>(16));
    }

    @Override
    public List<RoleDO> list(Long userId) {
        List<Long> rolesIds = userRoleDao.listRoleId(userId);
        List<RoleDO> roles = roleDao.list(new HashMap<>(16));
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

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Transactional
    @Override
    public int save(RoleDO role) {
        int count = roleDao.save(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuDao.removeByRoleId(roleId);
        if (!rms.isEmpty()) {
            roleMenuDao.batchSave(rms);
        }
        return count;
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Transactional
    @Override
    public int remove(Long id) {
        int count = roleDao.remove(id);
        roleMenuDao.removeByRoleId(id);
        return count;
    }

    @Override
    public RoleDO get(Long id) {
        RoleDO roleDO = roleDao.get(id);
        return roleDO;
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Override
    public int update(RoleDO role) {
        int r = roleDao.update(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        roleMenuDao.removeByRoleId(roleId);
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        if (!rms.isEmpty()) {
            roleMenuDao.batchSave(rms);
        }
        return r;
    }

    @Override
    public int batchremove(Long[] ids) {
        return roleDao.batchRemove(ids);
    }

}
