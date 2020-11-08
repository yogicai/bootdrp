package com.bootdo.se.controller;

import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.R;
import com.bootdo.se.service.SEOrderEntryService;
import com.bootdo.se.controller.request.SEOrderVO;
import com.bootdo.se.domain.SEOrderDO;
import com.bootdo.se.validator.SEOrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购货订单分录
 * 
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */
 
@Controller
@RequestMapping("/se/entry")
public class SEOrderEntryController {
    @Autowired
    private SEOrderValidator orderValidator;
	@Autowired
	private SEOrderEntryService orderEntryService;
	
	@GetMapping()
	@RequiresPermissions("se:entry:entry")
	String OrderEntry(){
	    return "se/entry/entry";
	}

    /**
     * 保存
     */
    @Log("销售单保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("se:entry:add")
    public R save(@RequestBody SEOrderVO order) {
        orderValidator.validateSave(order);
        SEOrderDO orderDO = orderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    /**
     * 选择零售客户
     */
    @GetMapping("/add")
    @RequiresPermissions("se:entry:add")
    String add(){
        return "se/entry/add";
    }


    @GetMapping("/addHead")
    @RequiresPermissions("se:entry:add")
    String addHead(){
        return "se/entry/addHead";
    }


    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("se:order:order")
    public R get(@RequestParam Map<String, Object> params){
        //查询列表数据
        SEOrderVO orderVO= orderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
