package com.bootdo.modular.system.controller;

import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.param.SysDeptParam;
import com.bootdo.modular.system.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门管理
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-27 14:40:36
 */
@Api(tags = "部门管理")
@Controller
@RequestMapping("/system/sysDept")
public class DeptController extends BaseController {
    @Resource
    private DeptService sysDeptService;


    @GetMapping()
    @RequiresPermissions("system:sysDept:sysDept")
    String dept() {
        return "system/dept/dept";
    }

    @ApiOperation(value = "获取部门列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("system:sysDept:sysDept")
    public List<DeptDO> list() {
        return sysDeptService.list(new SysDeptParam());
    }

    @GetMapping("/add/{pId}")
    @RequiresPermissions("system:sysDept:add")
    String add(@PathVariable("pId") Long pId, Model model) {
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "总部门");
        } else {
            model.addAttribute("pName", sysDeptService.getById(pId).getName());
        }
        return "system/dept/add";
    }

    @GetMapping("/edit/{deptId}")
    @RequiresPermissions("system:sysDept:edit")
    String edit(@PathVariable("deptId") Long deptId, Model model) {
        DeptDO sysDept = sysDeptService.getById(deptId);
        model.addAttribute("sysDept", sysDept);
        if (Constant.DEPT_ROOT_ID.equals(sysDept.getParentId())) {
            model.addAttribute("parentDeptName", "无");
        } else {
            DeptDO parDept = sysDeptService.getById(sysDept.getParentId());
            model.addAttribute("parentDeptName", parDept.getName());
        }
        return "system/dept/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("system:sysDept:add")
    public R save(DeptDO sysDept) {
        sysDeptService.save(sysDept);
        return R.ok();
    }

    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("system:sysDept:edit")
    public R update(DeptDO sysDept) {
        sysDeptService.updateById(sysDept);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("system:sysDept:remove")
    public R remove(Long deptId) {
        if (sysDeptService.checkDeptHasUser(deptId)) {
            return R.error("部门包含部门或用户,不允许修改");
        }
        sysDeptService.removeById(deptId);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("system:sysDept:batchRemove")
    public R remove(@RequestParam("ids[]") List<Integer> deptIds) {
        sysDeptService.removeByIds(deptIds);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        return sysDeptService.getTree();
    }

    @GetMapping("/treeView")
    String treeView() {
        return "system/dept/deptTree";
    }

}
