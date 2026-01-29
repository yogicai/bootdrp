package com.bootdo.core.security.realm;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.service.MenuService;
import com.bootdo.modular.system.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
/**
 * @author L
 * @since 2026-01-08 11:11
 */
public class BDUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userService.list(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username)).get(0);

        Assert.notNull(user, "用户不存在");
        // 填充用户信息
        user = userService.fillUserInfo(user);
        // 填充用户权限
        Set<String> permissions = menuService.listPerms(user.getUserId());

        return new BDUserDetails(user, permissions);
    }
}