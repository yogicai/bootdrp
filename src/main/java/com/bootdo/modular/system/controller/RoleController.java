package com.bootdo.modular.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.service.RoleService;
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
@Api(tags = "角色管理")
@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;


    @RequiresPermissions("sys:role:role")
    @GetMapping()
    String role() {
        return "system/role/role";
    }

    @RequiresPermissions("sys:role:role")
    @GetMapping("/list")
    @ResponseBody()
    List<RoleDO> list() {
        return roleService.list(Wrappers.query());
    }

    @Log("添加角色")
    @RequiresPermissions("sys:role:add")
    @GetMapping("/add")
    String add() {
        return "system/role/add";
    }

    @Log("编辑角色")
    @RequiresPermissions("sys:role:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        RoleDO roleDO = roleService.getById(id);
        model.addAttribute("role", roleDO);
        return "system/role/edit";
    }

    @Log("保存角色")
    @RequiresPermissions("sys:role:add")
    @PostMapping("/save")
    @ResponseBody()
    R save(RoleDO role) {
        roleService.saveRole(role);
        return R.ok();
    }

    @Log("更新角色")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/update")
    @ResponseBody()
    R update(RoleDO role) {
        roleService.updateRole(role);
        return R.ok();
    }

    @Log("删除角色")
    @RequiresPermissions("sys:role:remove")
    @PostMapping("/remove")
    @ResponseBody()
    R remove(Long id) {
        roleService.removeRole(id);
        return R.ok();
    }

    @RequiresPermissions("sys:role:batchRemove")
    @Log("批量删除角色")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") List<Long> ids) {
        roleService.removeBatchRole(ids);
        return R.ok();
    }
}
