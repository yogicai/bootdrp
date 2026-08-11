package com.bootdo.modular.system.controller;

import com.bootdo.core.utils.SecurityUtils;
import com.bootdo.modular.system.domain.UserDO;
import org.springframework.stereotype.Controller;

/**
 * @author L
 */
@Controller
public class BaseController {

    public UserDO getUser() {
        return SecurityUtils.getUser();
    }

    public Long getUserId() {
        return getUser().getUserId();
    }

    public String getUsername() {
        return getUser().getUsername();
    }

}