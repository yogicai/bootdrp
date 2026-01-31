package com.bootdo.core.utils;

import com.bootdo.core.annotation.DataScope.DataType;
import com.bootdo.core.security.realm.BDUserDetails;
import com.bootdo.modular.system.domain.UserDO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author L
 * @since 2026-01-08 11:11
 */
public class SecurityUtils {

    public final static ThreadLocal<Map<DataType, Collection<Long>>> SCOPE_DATA = ThreadLocal.withInitial(HashMap::new);

    public static void setScopes(DataType dataType, Collection<Long> scopes) {
        SCOPE_DATA.get().put(dataType, scopes);
    }

    public static Collection<Long> getScopes(DataType dataType) {
        return SCOPE_DATA.get().get(dataType);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDO getUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof BDUserDetails) {
            return ((BDUserDetails) authentication.getPrincipal()).getUser();
        }
        return null;
    }

    public static Long getUserId() {
        UserDO user = getUser();
        return user != null ? user.getUserId() : null;
    }

    public static String getUserName() {
        UserDO user = getUser();
        return user != null ? user.getUsername() : null;
    }

}
