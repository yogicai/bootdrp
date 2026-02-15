package com.bootdo.modular.system.controller;

import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.DeptDO;
import com.bootdo.modular.system.domain.RoleDO;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.system.param.SysUserParam;
import com.bootdo.modular.system.result.LoginUserResult;
import com.bootdo.modular.system.result.UploadImgResult;
import com.bootdo.modular.system.result.UserVO;
import com.bootdo.modular.system.service.DictService;
import com.bootdo.modular.system.service.RoleService;
import com.bootdo.modular.system.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author L
 */
@Tag(name = "用户管理")
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
    @PreAuthorize("hasAuthority('sys:user:user')")
    public String user(Model model) {
        return "system/user/user";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageR list(SysUserParam param) {
        return userService.page(param);
    }

    @LogRecord(value = "添加用户")
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('sys:user:add')")
    public String add(Model model) {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return "system/user/add";
    }

    @LogRecord(value = "编辑用户")
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public String edit(Model model, @PathVariable Long id) {
        UserDO userDO = userService.getUser(id);
        model.addAttribute("user", userDO);
        List<RoleDO> roles = roleService.list(id);
        model.addAttribute("roles", roles);
        return "system/user/edit";
    }

    @LogRecord(value = "保存用户")
    @PostMapping("/save")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:add')")
    public R<Void> saveUser(UserDO user) {
        userService.saveUser(user);
        return R.ok();
    }

    @LogRecord(value = "更新用户")
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public R<Void> update(UserDO user) {
        userService.updateUser(user);
        return R.ok();
    }

    @LogRecord(value = "更新用户")
    @PostMapping("/updatePersonal")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public R<Void> updatePersonal(UserDO user) {
        userService.updateById(user);
        return R.ok();
    }

    @LogRecord(value = "删除用户")
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:remove')")
    public R<Void> remove(Long id) {
        userService.removeUser(id);
        return R.ok();
    }

    @LogRecord(value = "批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:batchRemove')")
    public R<Void> batchRemove(@RequestParam("ids[]") List<Integer> userIds) {
        userService.batchRemove(userIds);
        return R.ok();
    }

    @PostMapping("/exit")
    @ResponseBody
    public R<Boolean> exit(@RequestParam String username) {
        // 存在，不通过，false
        return R.ok(!userService.exit(username));
    }

    @LogRecord(value = "请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    public String resetPwd(@PathVariable("id") Long userId, Model model) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        model.addAttribute("user", userDO);
        return "system/user/reset_pwd";
    }

    @LogRecord(value = "提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    public R<Void> resetPwd(UserVO userVO) throws Exception {
        userService.resetPwd(userVO, getUser());
        return R.ok();
    }

    @LogRecord(value = "admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    public R<Void> adminResetPwd(UserVO userVO) throws Exception {
        userService.adminResetPwd(userVO);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        return userService.getTree();
    }

    @GetMapping("/treeView")
    public String treeView() {
        return "system/user/userTree";
    }

    @GetMapping("/personal")
    public String personal(Model model) {
        UserDO userDO = userService.getUser(getUserId());
        model.addAttribute("user", userDO);
        model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
        model.addAttribute("sexList", dictService.getSexList());
        return "system/user/personal";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    public R<UploadImgResult> uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatarData) {
        return R.ok(userService.updatePersonalImg(file, avatarData, getUserId()));
    }

    @GetMapping("/loginUserInfo")
    @ResponseBody
    public LoginUserResult loginUserInfo() {
        return userService.loginUserInfo();
    }

}
