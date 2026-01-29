package com.bootdo.modular.system.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.security.context.CacheContextHolder;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.domain.UserOnline;
import jakarta.annotation.Resource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_IP;
import static com.bootdo.core.security.context.CacheContextHolder.SESSION_KEY_LOGIN_DATE;

/**
 * 在线用户管理服务
 *
 * @author bootdo
 */
@Service
public class SecuritySessionService {

    @Resource
    private SessionRegistry sessionRegistry;

    public List<UserOnline> list(Map<String, Object> params) {
        List<UserOnline> list = new ArrayList<>();
        String searchText = MapUtil.getStr(params, "searchText", "");

        // 获取所有会话信息
        Collection<SessionInformation> allSessions = getAllSessions();

        for (SessionInformation session : allSessions) {
            UserOnline userOnline = new UserOnline();
            // 获取用户信息
            Object principal = session.getPrincipal();
            if (principal instanceof UserDO userDO) {
                userOnline.setUsername(userDO.getUsername());
                userOnline.setUserId(userDO.getUserId().toString());
                // 搜索过滤
                if (!userOnline.getUsername().contains(searchText)) {
                    continue;
                }
            } else if (principal instanceof UserDetails userDetails) {
                userOnline.setUsername(userDetails.getUsername());
                // 搜索过滤
                if (!userOnline.getUsername().contains(searchText)) {
                    continue;
                }
            } else {
                continue;
            }
            // 设置会话信息
            userOnline.setId(session.getSessionId());
            userOnline.setLastAccessTime(session.getLastRequest());
            userOnline.setTimeout(30 * 60 * 1000L);
            userOnline.setStatus("on_line");

            // 获取扩展信息
            userOnline.setHost(CacheContextHolder.me().get(SESSION_KEY_IP, session.getSessionId()));
            userOnline.setStartTimestamp(CacheContextHolder.me().get(SESSION_KEY_LOGIN_DATE, session.getSessionId()));

            list.add(userOnline);
        }
        return list;
    }

    public Collection<SessionInformation> sessionList() {
        return getAllSessions();
    }

    public void forceLogout(String sessionId) {
        getAllSessions().stream()
                .filter(session -> session.getSessionId().equals(sessionId))
                .forEach(session -> {
                    // 1. 标记会话过期
                    session.expireNow();
                    // 2. 从注册表中移除
                    sessionRegistry.removeSessionInformation(sessionId);
                    // 3. 从缓存中移除会话相关信息
                    CacheContextHolder.me().remove(SESSION_KEY_IP, session.getSessionId());
                    CacheContextHolder.me().remove(SESSION_KEY_LOGIN_DATE, session.getSessionId());
                });
    }

    /**
     * 获取所有会话信息
     */
    private Collection<SessionInformation> getAllSessions() {
        Set<SessionInformation> allSessions = new HashSet<>();
        // 获取所有用户主体
        List<Object> principals = sessionRegistry.getAllPrincipals();

        // 为每个用户获取会话信息
        for (Object principal : principals) {
            allSessions.addAll(sessionRegistry.getAllSessions(principal, false));
        }

        return allSessions;
    }
}
