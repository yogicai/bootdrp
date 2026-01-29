package com.bootdo.core.security.handler;

import com.bootdo.core.security.context.CacheContextHolder;
import com.bootdo.core.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_IP;
import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_LOGIN_DATE;

/**
 * 与 Spring Security 默认登出功能配合使用，仅处理系统额外的业务逻辑
 * Spring Security 已自动处理：清除 SecurityContext、使 HttpSession 失效、删除 JSESSIONID Cookie
 *
 * @author L
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. 清除 ThreadLocal 数据 - 系统额外业务逻辑
        // 对应 SecurityUtils.logout() 中的 SCOPE_DATA.remove()
        SecurityUtils.SCOPE_DATA.remove();

        // 2. 清除缓存中的用户登录信息 - 系统额外业务逻辑
        // 对应 SecurityUtils.logout() 中的 CacheContextHolder 操作
        String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : null;
        if (sessionId != null) {
            CacheContextHolder.me().remove(SESSION_KEY_IP, sessionId);
            CacheContextHolder.me().remove(SESSION_KEY_LOGIN_DATE, sessionId);
        }

        // 3. 执行重定向 - 关键步骤
        // 重定向到登录页面，与原来的 logoutSuccessUrl("/login") 效果一致
        response.sendRedirect("/login");
    }
}
