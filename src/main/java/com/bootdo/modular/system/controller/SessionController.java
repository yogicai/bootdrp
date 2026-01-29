package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.UserOnline;
import com.bootdo.modular.system.service.SecuritySessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Tag(name = "在线用户")
@RequestMapping("/sys/online")
@Controller
public class SessionController {
    @Resource
    private SecuritySessionService sessionService;

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
    public R forceLogout(@PathVariable String sessionId) {
        sessionService.forceLogout(sessionId);
        return R.ok();
    }

    @ResponseBody
    @RequestMapping("/sessionList")
    public Collection<SessionInformation> sessionList() {
        return sessionService.sessionList();
    }
}
