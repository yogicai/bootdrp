package com.bootdo.core.utils;

import com.bootdo.core.annotation.DataScope.DataType;
import com.bootdo.modular.system.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author L
 */
public class ShiroUtils {

    public final static ThreadLocal<Map<DataType, Collection<Long>>> SCOPE_DATA = ThreadLocal.withInitial(HashMap::new);

    public static void setScopes(DataType dataType, Collection<Long> scopes) {
        SCOPE_DATA.get().put(dataType, scopes);
    }

    public static Collection<Long> getScopes(DataType dataType) {
        return SCOPE_DATA.get().get(dataType);
    }

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
