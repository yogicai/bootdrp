package com.bootdo.modular.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.service.RoleService;
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
@Tag(name = "角色管理")
@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;


    @PreAuthorize("hasAuthority('sys:role:role')")
    @GetMapping()
    String role() {
        return "system/role/role";
    }

    @PreAuthorize("hasAuthority('sys:role:role')")
    @GetMapping("/list")
    @ResponseBody()
    List<RoleDO> list() {
        return roleService.list(Wrappers.query());
    }

    @LogRecord(value = "添加角色")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @GetMapping("/add")
    String add() {
        return "system/role/add";
    }

    @LogRecord(value = "编辑角色")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        RoleDO roleDO = roleService.getById(id);
        model.addAttribute("role", roleDO);
        return "system/role/edit";
    }

    @LogRecord(value = "保存角色")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping("/save")
    @ResponseBody()
    R save(RoleDO role) {
        roleService.saveRole(role);
        return R.ok();
    }

    @LogRecord(value = "更新角色")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PostMapping("/update")
    @ResponseBody()
    R update(RoleDO role) {
        roleService.updateRole(role);
        return R.ok();
    }

    @LogRecord(value = "删除角色")
    @PreAuthorize("hasAuthority('sys:role:remove')")
    @PostMapping("/remove")
    @ResponseBody()
    R remove(Long id) {
        roleService.removeRole(id);
        return R.ok();
    }

    @PreAuthorize("hasAuthority('sys:role:batchRemove')")
    @LogRecord(value = "批量删除角色")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") List<Long> ids) {
        roleService.removeBatchRole(ids);
        return R.ok();
    }
}
