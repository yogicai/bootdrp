package com.bootdo.se.controller;

import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.PageJQUtils;
import com.bootdo.common.utils.QueryJQ;
import com.bootdo.common.utils.R;
import com.bootdo.se.domain.SEOrderDO;
import com.bootdo.se.service.SEOrderService;
import com.bootdo.se.validator.SEOrderValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购货订单
 * 
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */
 
@Controller
@RequestMapping("/se/order")
public class SEOrderController {
    @Autowired
    private SEOrderValidator orderValidator;
	@Autowired
	private SEOrderService orderService;
	
	@GetMapping()
	@RequiresPermissions("se:order:order")
	String Order(){
	    return "se/order/order";
	}

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @RequiresPermissions("se:order:order")
    public PageJQUtils listP(@RequestBody Map<String, Object> params){
        return list(params);
    }

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("se:order:order")
	public PageJQUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<SEOrderDO> orderList = orderService.list(query);
        int total = orderService.count(query);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        PageJQUtils pageUtils = new PageJQUtils(orderList, totalPage, query.getPage(), total);
		return pageUtils;
	}


    /**
     * 审核、反审核
     */
    @Log("销售单审核、反审核")
    @PostMapping( "/audit")
    @ResponseBody
    @RequiresPermissions("se:order:audit")
    public R audit(@RequestBody Map<String, Object> params){
        orderValidator.validateAudit(params);
        orderService.audit(params);
        return R.ok();
    }

	/**
	 * 删除
	 */
    @Log("销售单删除")
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("se:order:remove")
	public R remove(@RequestParam("billNos[]")List<String> billNos){
        orderValidator.validateRemove(billNos);
        orderService.batchRemove(billNos);
        return R.ok();
	}
}
