package com.bootdo.modular.system.dao;

import com.bootdo.modular.system.domain.UserRoleDO;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-10-03 11:08:59
 */
public interface UserRoleDao extends MPJBaseMapper<UserRoleDO> {

    List<Long> listRoleId(Long userId);

    int removeByUserId(Long userId);

    int batchSave(List<UserRoleDO> list);

    int batchRemoveByUserId(List<Integer> ids);
}
