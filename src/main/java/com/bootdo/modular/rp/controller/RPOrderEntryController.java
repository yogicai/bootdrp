package com.bootdo.modular.rp.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.param.RPOrderVO;
import com.bootdo.modular.rp.service.RPOrderEntryService;
import com.bootdo.modular.rp.validator.RPOrderValidator;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */
@Api(tags = "收款、付款订单")
@Controller
@RequestMapping("/rp/entry")
public class RPOrderEntryController {
    @Resource
    private RPOrderValidator rpOrderValidator;
    @Resource
    private RPOrderEntryService rpOrderEntryService;

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
        RPOrderEntryDO orderEntry = rpOrderEntryService.get(id);
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
        rpOrderValidator.validateSave(order);
        RPOrderDO orderDO = rpOrderEntryService.save(order);
        return R.ok(ImmutableMap.of("billNo", orderDO.getBillNo()));
    }

    @ResponseBody
    @GetMapping("/get")
    @RequiresPermissions("rp:order:order")
    public R get(@RequestParam Map<String, Object> params) {
        //查询列表数据
        RPOrderVO orderVO = rpOrderEntryService.getOrderVO(params);
        return R.ok().put("order", orderVO);
    }
}
