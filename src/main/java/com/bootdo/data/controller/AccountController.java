package com.bootdo.data.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.service.AccountService;
import com.bootdo.data.validator.DataValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

 /**
 * @Author: yogiCai
 * @Date: 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/account")
public class AccountController extends BaseController {
     @Autowired
     private DataValidator dataValidator;
	@Autowired
	private AccountService accountService;
	
	@GetMapping()
	@RequiresPermissions("data:account:account")
	String Account(){
	    return "data/account/account";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("data:account:account")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<AccountDO> accountList = accountService.list(query);
		int total = accountService.count(query);
		PageUtils pageUtils = new PageUtils(accountList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("data:account:add")
	String add(){
	    return "data/account/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("data:account:edit")
	String edit(@PathVariable("id") Integer id,Model model){
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
	public R save( AccountDO account){
        dataValidator.validateAccount(account);
		if(accountService.save(account)>0){
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
	public R update( AccountDO account){
        dataValidator.validateAccount(account);
		accountService.update(account);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("data:account:remove")
	public R remove( Integer id){
		if(accountService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("data:account:remove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		accountService.batchRemove(ids);
		return R.ok();
	}
	
}
