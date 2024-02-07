package com.bootdo.modular.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.modular.system.dao.UserRoleDao;
import com.bootdo.modular.system.domain.UserRoleDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author L
 * @since 2024-02-06 16:29
 */
@Service
public class UserRoleService extends ServiceImpl<UserRoleDao, UserRoleDO> {

    public List<Long> listRoleId(Long userId) {
        return this.list(Wrappers.lambdaQuery(UserRoleDO.class).select(UserRoleDO::getRoleId).eq(UserRoleDO::getUserId, userId))
                .stream()
                .map(UserRoleDO::getRoleId).collect(Collectors.toList());
    }

    public boolean removeByUserId(Long userId) {
        return this.remove(Wrappers.lambdaQuery(UserRoleDO.class).eq(UserRoleDO::getUserId, userId));
    }


    public boolean removeByUserId(List<Integer> userIds) {
        return this.remove(Wrappers.lambdaQuery(UserRoleDO.class).in(UserRoleDO::getUserId, userIds));
    }

}
