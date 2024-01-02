package com.bootdo.system.controller;

import com.bootdo.common.utils.R;
import com.bootdo.system.domain.UserOnline;
import com.bootdo.system.service.SessionService;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@RequestMapping("/sys/online")
@Controller
public class SessionController {
	@Resource
	SessionService sessionService;

	@GetMapping()
	public String online() {
		return "system/online/online";
	}

	@ResponseBody
	@RequestMapping("/list")
	public List<UserOnline> list(@RequestParam Map<String, Object> params) {
		return sessionService.list(params);
	}

	@ResponseBody
	@RequestMapping("/forceLogout/{sessionId}")
	public R forceLogout(@PathVariable("sessionId") String sessionId, RedirectAttributes redirectAttributes) {
		try {
			sessionService.forceLogout(sessionId);
			return R.ok();
		} catch (Exception e) {
			return R.error();
		}

	}

	@ResponseBody
	@RequestMapping("/sessionList")
	public Collection<Session> sessionList() {
		return sessionService.sessionList();
	}
}
