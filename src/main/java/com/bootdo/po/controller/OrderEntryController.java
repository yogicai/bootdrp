package com.bootdo.po.controller;

import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.R;
import com.bootdo.po.controller.request.OrderVO;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.po.service.OrderEntryService;
import com.bootdo.po.validator.OrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购货订单分录
 * @Author: yogiCai
 * @date 2018-01-21 12:38:44
 */

@Controller
@RequestMapping("/po/entry")
public class OrderEntryController {
    @Autowired
    private OrderValidator orderValidator;
    @Autowired
    private OrderEntryService orderEntryService;

    @GetMapping()
    @RequiresPermissions("po:entry:entry")
    String OrderEntry(@RequestParam Map<String, Object> params, Model model){
        model.addAttribute("billType", MapUtils.getString(params, "billType"));
        return "po/entry/entry";
    }

    /**
     * 保存
     */
    @Log("采购单保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("po:entry:add")
    public R save(@RequestBody OrderVO order) {
        orderValidator.validateSave(order);
        OrderDO orderDO = orderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    /**
     * 选择供应商弹窗
     */
    @GetMapping("/add")
    @RequiresPermissions("po:entry:add")
    String add(){
        return "po/entry/add";
    }


    @GetMapping("/addVendor")
    @RequiresPermissions("po:entry:add")
    String addHead(){
        return "po/entry/addVendor";
    }


    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("po:order:order")
    public R get(@RequestParam Map<String, Object> params){
        //查询列表数据
        OrderVO orderVO= orderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
