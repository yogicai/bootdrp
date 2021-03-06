package com.bootdo.rp.controller;

import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.PageJQUtils;
import com.bootdo.common.utils.QueryJQ;
import com.bootdo.common.utils.R;
import com.bootdo.rp.domain.PointEntryDO;
import com.bootdo.rp.service.RPPointService;
import com.bootdo.rp.validator.RPPointValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户积分
 * @author yogiCai
 * @date 2018-03-06 23:17:49
 */
 
@Controller
@RequestMapping("/rp/point")
public class RPPointController {
    @Autowired
    private RPPointValidator pointValidator;
	@Autowired
	private RPPointService pointService;

	
	@GetMapping()
	@RequiresPermissions("rp:point:point")
	String Point(@RequestParam Map<String, Object> params, Model model){
	    return "rp/point/point";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("rp:point:point")
	public PageJQUtils list(@RequestParam Map<String, Object> params){
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<PointEntryDO> orderList = pointService.list(query);
        int total = pointService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        PageJQUtils pageUtils = new PageJQUtils(orderList, totalPage, query.getPage(), total);
        return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("rp:point:add")
	String add(){
	    return "rp/point/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("rp:point:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		PointEntryDO pointEntry = pointService.get(id);
		model.addAttribute("pointEntry", pointEntry);
	    return "rp/point/edit";
	}
	
	/**
	 * 保存
	 */
    @Log("积分保存")
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("rp:point:add")
	public R save( PointEntryDO pointEntry){
        pointValidator.validateSave(pointEntry);
		if(pointService.save(pointEntry)>0){
			return R.ok();
		}
		return R.error();
	}

    /**
     * 修改
     */
    @Log("积分修改")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("rp:point:edit")
    public R update( PointEntryDO pointEntry){
        pointValidator.validateSave(pointEntry);
        pointService.update(pointEntry);
        return R.ok();
    }

	/**
	 * 删除
	 */
    @Log("积分删除")
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("rp:point:remove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		pointService.batchRemove(ids);
		return R.ok();
	}
}
