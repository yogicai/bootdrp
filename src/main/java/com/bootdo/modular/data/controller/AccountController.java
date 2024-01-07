package com.bootdo.modular.data.controller;

import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.service.AccountService;
import com.bootdo.modular.data.validator.DataValidator;
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

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:account:add")
    public R save(AccountDO account) {
        dataValidator.validateAccount(account);
        if (accountService.save(account) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("data:account:edit")
    public R update(AccountDO account) {
        dataValidator.validateAccount(account);
        accountService.update(account);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:account:remove")
    public R remove(Integer id) {
        if (accountService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:account:remove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        accountService.batchRemove(ids);
        return R.ok();
    }

}