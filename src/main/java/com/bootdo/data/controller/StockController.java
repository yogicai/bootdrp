package com.bootdo.data.controller;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.data.domain.StockDO;
import com.bootdo.data.service.StockService;
import com.bootdo.data.validator.DataValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 仓库表
 * @author yogiCai
 * @date 2018-02-18 16:23:32
 */
 
@Controller
@RequestMapping("/data/stock")
public class StockController {
    @Autowired
    private DataValidator dataValidator;
	@Autowired
	private StockService stockService;
	
	@GetMapping()
	@RequiresPermissions("data:stock:stock")
	String Stock(){
	    return "data/stock/stock";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("data:stock:stock")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<StockDO> stockList = stockService.list(query);
		int total = stockService.count(query);
		PageUtils pageUtils = new PageUtils(stockList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("data:stock:add")
	String add(){
	    return "data/stock/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("data:stock:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		StockDO stock = stockService.get(id);
		model.addAttribute("stock", stock);
	    return "data/stock/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("data:stock:add")
	public R save( StockDO stock){
        dataValidator.validateStock(stock);
		if(stockService.save(stock)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("data:stock:edit")
	public R update( StockDO stock){
        dataValidator.validateStock(stock);
		stockService.update(stock);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("data:stock:remove")
	public R remove( Integer id){
		if(stockService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("data:stock:remove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		stockService.batchRemove(ids);
		return R.ok();
	}
	
}
