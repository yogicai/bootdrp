package com.bootdo.modular.se.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.se.service.SEOrderEntryService;
import com.bootdo.modular.se.validator.SEOrderValidator;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @since 2018-02-18 16:50:26
 */
@Api(tags = "销售订单")
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

    @Log("销售单保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("se:entry:add")
    public R save(@RequestBody @Validated SEOrderVO order) {
        seOrderValidator.validateSave(order);
        SEOrderDO orderDO = seOrderEntryService.save(order);
        return R.ok(MapUtil.of("billNo", orderDO.getBillNo()));
    }

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
    public R get(@Validated OrderDetailParam param) {
        //查询列表数据
        SEOrderVO orderVO = seOrderEntryService.getOrderVO(param);
        return R.ok().put("order", orderVO);
    }
}
