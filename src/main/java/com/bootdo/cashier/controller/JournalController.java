package com.bootdo.cashier.controller;

import com.bootdo.cashier.domain.JournalDO;
import com.bootdo.cashier.service.JournalService;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.R;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 日记账
 *
 * @author yogiCai
 * @date 2018-07-14 22:31:58
 */
 
@Controller
@RequestMapping("/cashier/journal")
public class JournalController {
	@Resource
	private JournalService journalService;
	
	@GetMapping()
	@RequiresPermissions("cashier:journal:journal")
	String Journal(){
	    return "cashier/journal/journal";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("cashier:journal:journal")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        params.put("start", DateUtils.getMonthBegin(MapUtils.getString(params, "start")));
        params.put("end", DateUtils.getMonthEnd(MapUtils.getString(params, "end")));
        List<JournalDO> dataList = journalService.list(params);
        return R.ok(ImmutableMap.of("rows", dataList));
	}


    @GetMapping("/add")
    @RequiresPermissions("cashier:journal:journal")
    String add(){
        return "cashier/journal/add";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("cashier:journal:journal")
    String edit(@PathVariable("id") Integer id,Model model){
        JournalDO journalDO = journalService.get(id);
        model.addAttribute("journal", journalDO);
        return "cashier/journal/edit";
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("cashier:journal:journal")
    public R update( JournalDO journal){
        journalService.update(journal);
        return R.ok();
    }

	/**
	 * 保存
	 */
    @Log("新增-修改日记账")
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("cashier:journal:journal")
	public R save( JournalDO journal){
		if(journalService.save(journal)>0){
			return R.ok();
		}
		return R.error();
	}


	/**
	 * 删除
	 */
    @Log("删除日记账")
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("cashier:journal:journal")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		journalService.batchRemove(ids);
		return R.ok();
	}
	
}
