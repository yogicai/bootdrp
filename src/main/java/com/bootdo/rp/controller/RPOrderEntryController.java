package com.bootdo.rp.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.utils.R;
import com.bootdo.rp.controller.request.RPOrderVO;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.service.RPOrderEntryService;
import com.bootdo.rp.validator.RPOrderValidator;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */

@Controller
@RequestMapping("/rp/entry")
public class RPOrderEntryController {
    @Autowired
    private RPOrderValidator orderValidator;
    @Autowired
    private RPOrderEntryService orderEntryService;

    @GetMapping()
    @RequiresPermissions("rp:entry:entry")
    public String orderEntry(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "rp/entry/entry";
    }

    @GetMapping("/add")
    @RequiresPermissions("rp:entry:add")
    public String add() {
        return "rp/entry/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("rp:entry:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        RPOrderEntryDO orderEntry = orderEntryService.get(id);
        model.addAttribute("entry", orderEntry);
        return "rp/entry/edit";
    }

    /**
     * 保存
     */
    @Log("财务单保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rp:entry:add")
    public R save(@RequestBody RPOrderVO order) {
        orderValidator.validateSave(order);
        RPOrderDO orderDO = orderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("rp:order:order")
    public R get(@RequestParam Map<String, Object> params) {
        //查询列表数据
        RPOrderVO orderVO = orderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
