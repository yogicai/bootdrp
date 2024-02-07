package com.bootdo.modular.system.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.MD5Utils;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.param.SysUserParam;
import com.bootdo.modular.system.result.LoginUserResult;
import com.bootdo.modular.system.result.UserVO;
import com.bootdo.modular.system.service.DictService;
import com.bootdo.modular.system.service.RoleService;
import com.bootdo.modular.system.service.UserService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Api(tags = "用户管理")
@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private DictService dictService;


    @GetMapping("")
    @RequiresPermissions("sys:user:user")
    String user(Model model) {
        return "system/user/user";
    }

    @GetMapping("/list")
    @ResponseBody
    PageR list(SysUserParam param) {
        // 查询列表数据
        return userService.page(param);
    }

    @Log("添加用户")
    @GetMapping("/add")
    @RequiresPermissions("sys:user:add")
    String add(Model model) {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return "system/user/add";
    }

    @Log("编辑用户")
    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:user:edit")
    String edit(Model model, @PathVariable("id") Long id) {
        UserDO userDO = userService.getUser(id);
        model.addAttribute("user", userDO);
        List<RoleDO> roles = roleService.list(id);
        model.addAttribute("roles", roles);
        return "system/user/edit";
    }

    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys:user:add")
    R save(UserDO user) {
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        userService.save(user);
        return R.ok();
    }

    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    R update(UserDO user) {
        userService.update(user);
        return R.ok();
    }

    @Log("更新用户")
    @PostMapping("/updatePersonal")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    R updatePersonal(UserDO user) {
        userService.updateById(user);
        return R.ok();
    }

    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:user:remove")
    R remove(Long id) {
        userService.removeUser(id);
        return R.ok();
    }

    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:user:batchRemove")
    R batchRemove(@RequestParam("ids[]") List<Integer> userIds) {
        userService.batchRemove(userIds);
        return R.ok();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam String username) {
        // 存在，不通过，false
        return !userService.exit(username);
    }

    @Log("请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    @RequiresPermissions("sys:user:resetPwd")
    String resetPwd(@PathVariable("id") Long userId, Model model) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        model.addAttribute("user", userDO);
        return "system/user/reset_pwd";
    }

    @Log("提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    R resetPwd(UserVO userVO) throws Exception {
        userService.resetPwd(userVO, getUser());
        return R.ok();
    }

    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    @RequiresPermissions("sys:user:resetPwd")
    R adminResetPwd(UserVO userVO) throws Exception {
        userService.adminResetPwd(userVO);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        return userService.getTree();
    }

    @GetMapping("/treeView")
    String treeView() {
        return "system/user/userTree";
    }

    @GetMapping("/personal")
    String personal(Model model) {
        UserDO userDO = userService.getUser(getUserId());
        model.addAttribute("user", userDO);
        model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
        model.addAttribute("sexList", dictService.getSexList());
        return "system/user/personal";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data) {
        Map<String, Object> result;
        try {
            result = userService.updatePersonalImg(file, avatar_data, getUserId());
        } catch (Exception e) {
            return R.error("更新图像失败！");
        }
        if (result != null && !result.isEmpty()) {
            return R.ok(result);
        } else {
            return R.error("更新图像失败！");
        }
    }

    @GetMapping("/loginUserInfo")
    @ResponseBody
    public LoginUserResult loginUserInfo() {
        return userService.loginUserInfo();
    }

}
