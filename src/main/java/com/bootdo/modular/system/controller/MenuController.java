package com.bootdo.modular.system.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.MenuDO;
import com.bootdo.modular.system.param.SysMenuParam;
import com.bootdo.modular.system.service.MenuService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author L
 */
@Api(tags = "系统菜单")
@RequestMapping("/sys/menu")
@Controller
public class MenuController extends BaseController {
    @Resource
    private MenuService menuService;


    @RequiresPermissions("sys:menu:menu")
    @GetMapping()
    String menu() {
        return "system/menu/menu";
    }

    @RequiresPermissions("sys:menu:menu")
    @RequestMapping("/list")
    @ResponseBody
    List<MenuDO> list() {
        return menuService.list(new SysMenuParam());
    }

    @Log("添加菜单")
    @RequiresPermissions("sys:menu:add")
    @GetMapping("/add/{pId}")
    String add(Model model, @PathVariable("pId") Long pId) {
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.getById(pId).getName());
        }
        return "system/menu/add";
    }

    @Log("编辑菜单")
    @RequiresPermissions("sys:menu:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        MenuDO mdo = menuService.getById(id);
        Long pId = mdo.getParentId();
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.getById(pId).getName());
        }
        model.addAttribute("menu", mdo);
        return "system/menu/edit";
    }

    @Log("保存菜单")
    @RequiresPermissions("sys:menu:add")
    @PostMapping("/save")
    @ResponseBody
    R save(MenuDO menu) {
        menuService.save(menu);
        return R.ok();
    }

    @Log("更新菜单")
    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(MenuDO menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @Log("删除菜单")
    @RequiresPermissions("sys:menu:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id) {
        menuService.removeById(id);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    Tree<MenuDO> tree() {
        return menuService.getTree();
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Tree<MenuDO> tree(@PathVariable("roleId") Long roleId) {
        return menuService.getTree(roleId);
    }
}
