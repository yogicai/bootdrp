package com.bootdo.modular.se.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.se.controller.request.SEOrderVO;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.service.SEOrderEntryService;
import com.bootdo.modular.se.validator.SEOrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource
    private SEOrderValidator seOrderValidator;
    @Resource
    private SEOrderEntryService seOrderEntryService;

    @GetMapping()
    @RequiresPermissions("se:entry:entry")
    public String orderEntry() {
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
        seOrderValidator.validateSave(order);
        SEOrderDO orderDO = seOrderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    /**
     * 选择零售客户
     */
    @GetMapping("/add")
    @RequiresPermissions("se:entry:add")
    public String add() {
        return "se/entry/add";
    }


    @GetMapping("/addHead")
    @RequiresPermissions("se:entry:add")
    public String addHead() {
        return "se/entry/addHead";
    }


    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("se:order:order")
    public R get(@RequestParam Map<String, Object> params) {
        //查询列表数据
        SEOrderVO orderVO = seOrderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
