package com.bootdo.modular.data.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.param.AccountQryParam;
import com.bootdo.modular.data.service.AccountService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Api(tags = "帐户管理")
@Controller
@RequestMapping("/data/account")
public class AccountController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private AccountService accountService;

    @GetMapping()
    public String account() {
        return "data/account/account";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public PageR list(AccountQryParam param) {
        //查询列表数据
        return accountService.page(param);
    }

    @GetMapping("/add")
    public String add() {
        return "data/account/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        AccountDO account = accountService.getById(id);
        model.addAttribute("account", account);
        return "data/account/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public R save(@Validated AccountDO account) {
        dataValidator.validateAccount(account);
        accountService.add(account);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(@Validated(edit.class) AccountDO account) {
        dataValidator.validateAccount(account);
        accountService.updateById(account);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("data:account:remove")
    public R remove(Integer id) {
        accountService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:account:remove")
    public R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        accountService.removeByIds(ids);
        return R.ok();
    }

}
