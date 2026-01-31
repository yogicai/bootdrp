package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.node.Tree;
import com.bootdo.modular.system.domain.FileDO;
import com.bootdo.modular.system.domain.MenuDO;
import com.bootdo.modular.system.service.FileService;
import com.bootdo.modular.system.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author L
 */
@Tag(name = "登录管理")
@Controller
public class LoginController extends BaseController {
    @Resource
    private MenuService menuService;
    @Resource
    private FileService fileService;

    @GetMapping({"/", ""})
    String welcome(Model model) {
        return "redirect:/blog";
    }

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

    @GetMapping("/main")
    String workbench() {
        return "main";
    }

}
