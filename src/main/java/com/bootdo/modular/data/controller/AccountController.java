package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.service.AccountService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @date 2018-02-16 16:30:26
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
    @RequiresPermissions("data:account:account")
    public String account() {
        return "data/account/account";
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    @RequiresPermissions("data:account:account")
    public PageR list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AccountDO> accountList = accountService.list(query);
        int total = accountService.count(query);
        return new PageR(accountList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:account:add")
    public String add() {
        return "data/account/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:account:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        AccountDO account = accountService.get(id);
        model.addAttribute("account", account);
        return "data/account/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @RequiresPermissions("data:account:add")
    public R save(AccountDO account) {
        dataValidator.validateAccount(account);
        if (accountService.save(account) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    @RequiresPermissions("data:account:edit")
    public R update(AccountDO account) {
        dataValidator.validateAccount(account);
        accountService.update(account);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("data:account:remove")
    public R remove(Integer id) {
        if (accountService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:account:remove")
    public R batchRemove(@RequestParam("ids[]") Integer[] ids) {
        accountService.batchRemove(ids);
        return R.ok();
    }

}
