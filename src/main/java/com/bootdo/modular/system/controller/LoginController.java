package com.bootdo.modular.system.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.MD5Utils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.domain.MenuDO;
import com.bootdo.modular.system.service.FileService;
import com.bootdo.modular.system.service.MenuService;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author L
 */
@Api(tags = "登录管理")
@Controller
public class LoginController extends BaseController {
    @Resource
    MenuService menuService;
    @Resource
    FileService fileService;

    @GetMapping({"/", ""})
    String welcome(Model model) {
        return "redirect:/blog";
    }

    @Log("请求访问主页")
    @GetMapping({"/index"})
    String index(Model model) {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.getById(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null) {
            if (fileService.isExist(fileDO.getUrl())) {
                model.addAttribute("picUrl", fileDO.getUrl());
            } else {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        } else {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());
        return "index_v1";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password) {
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return R.ok();
        } catch (AuthenticationException e) {
            return R.error("用户或密码错误");
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String main() {
        return "main";
    }

    @GetMapping("/403")
    String error403() {
        return "system/error/403";
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.encrypt("L", "L@123"));
    }
}
