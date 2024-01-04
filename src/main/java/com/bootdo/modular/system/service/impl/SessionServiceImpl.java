package com.bootdo.modular.system.service.impl;

import cn.hutool.core.map.MapUtil;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.domain.UserOnline;
import com.bootdo.modular.system.service.SessionService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 待完善
 * 
 * @author bootdo
 *
 */
@Service
public class SessionServiceImpl implements SessionService {
	@Resource
	private SessionDAO sessionDAO;

	@Override
	public List<UserOnline> list(Map<String, Object> params) {
		List<UserOnline> list = new ArrayList<>();
        String searchText = MapUtil.getStr(params, "searchText", "");
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for (Session session : sessions) {
			UserOnline userOnline = new UserOnline();
			UserDO userDO = new UserDO();
			SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
			if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
				continue;
			} else {
				principalCollection = (SimplePrincipalCollection) session
						.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				userDO = (UserDO) principalCollection.getPrimaryPrincipal();
				userOnline.setUsername(userDO.getUsername());
			}
			if (!userOnline.getUsername().contains(searchText)) {
                continue;
            }
			userOnline.setId((String) session.getId());
			userOnline.setHost(session.getHost());
			userOnline.setStartTimestamp(session.getStartTimestamp());
			userOnline.setLastAccessTime(session.getLastAccessTime());
			userOnline.setTimeout(session.getTimeout());
			list.add(userOnline);
		}
		return list;
	}

	@Override
	public Collection<Session> sessionList() {
		return sessionDAO.getActiveSessions();
	}

	@Override
	public boolean forceLogout(String sessionId) {
		Session session = sessionDAO.readSession(sessionId);
		session.setTimeout(0);
		return true;
	}
}
