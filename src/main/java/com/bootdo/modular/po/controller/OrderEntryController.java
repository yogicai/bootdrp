package com.bootdo.modular.po.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.po.param.OrderVO;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.service.OrderEntryService;
import com.bootdo.modular.po.validator.OrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @date 2018-01-21 12:38:44
 */
@Controller
@RequestMapping("/po/entry")
public class OrderEntryController {
    @Resource
    private OrderValidator orderValidator;
    @Resource
    private OrderEntryService orderEntryService;

    @GetMapping()
    @RequiresPermissions("po:entry:entry")
    public String orderEntry(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
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
    public String add() {
        return "po/entry/add";
    }


    @GetMapping("/addVendor")
    @RequiresPermissions("po:entry:add")
    public String addHead() {
        return "po/entry/addVendor";
    }


    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("po:order:order")
    public R get(@RequestParam Map<String, Object> params) {
        //查询列表数据
        OrderVO orderVO = orderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
