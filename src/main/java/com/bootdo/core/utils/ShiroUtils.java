package com.bootdo.core.utils;

import com.bootdo.modular.system.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author L
 */
public class ShiroUtils {
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static UserDO getUser() {
        return (UserDO) getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    public static void logout() {
        getSubject().logout();
    }
}
