package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.RoleMenuDO;

import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 11:08:59
 */
public interface RoleMenuDao {

    RoleMenuDO get(Long id);

    List<RoleMenuDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RoleMenuDO roleMenu);

    int update(RoleMenuDO roleMenu);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Long> listMenuIdByRoleId(Long roleId);

    int removeByRoleId(Long roleId);

    int batchSave(List<RoleMenuDO> list);
}
