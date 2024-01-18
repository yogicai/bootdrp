package com.bootdo.modular.system.service;

import com.bootdo.modular.system.domain.UserOnline;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface SessionService {
    List<UserOnline> list(Map<String, Object> params);

    Collection<Session> sessionList();

    boolean forceLogout(String sessionId);
}
