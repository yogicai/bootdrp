package com.bootdo.modular.system.controller;

import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.MenuDO;
import com.bootdo.modular.system.param.SysMenuParam;
import com.bootdo.modular.system.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author L
 */
@Tag(name = "系统菜单")
@RequestMapping("/sys/menu")
@Controller
public class MenuController extends BaseController {
    @Resource
    private MenuService menuService;


    @PreAuthorize("hasAuthority('sys:menu:menu')")
    @GetMapping()
    public String menu() {
        return "system/menu/menu";
    }

    @PreAuthorize("hasAuthority('sys:menu:menu')")
    @RequestMapping("/list")
    @ResponseBody
    public List<MenuDO> list() {
        return menuService.list(new SysMenuParam());
    }

    @LogRecord(value = "添加菜单")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @GetMapping("/add/{pId}")
    public String add(Model model, @PathVariable Long pId) {
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.getById(pId).getName());
        }
        return "system/menu/add";
    }

    @LogRecord(value = "编辑菜单")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
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

    @LogRecord(value = "保存菜单")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/save")
    @ResponseBody
    public R<Void> save(MenuDO menu) {
        menuService.save(menu);
        return R.ok();
    }

    @LogRecord(value = "更新菜单")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @PostMapping("/update")
    @ResponseBody
    public R<Void> update(MenuDO menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @LogRecord(value = "删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:remove')")
    @PostMapping("/remove")
    @ResponseBody
    public R<Void> remove(Long id) {
        menuService.removeById(id);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<MenuDO> tree() {
        return menuService.getTree();
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    public Tree<MenuDO> tree(@PathVariable Long roleId) {
        return menuService.getTree(roleId);
    }
}
