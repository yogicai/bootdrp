package com.bootdo.modular.rp.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.param.RPOrderVO;
import com.bootdo.modular.rp.service.RPOrderEntryService;
import com.bootdo.modular.rp.validator.RPOrderValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 应收、应付票据核销目标单据
 *
 * @author yogiCai
 * @since 2018-02-21 21:23:27
 */
@Tag(name = "收款、付款订单")
@Controller
@RequestMapping("/rp/entry")
public class RPOrderEntryController {
    @Resource
    private RPOrderValidator rpOrderValidator;
    @Resource
    private RPOrderEntryService rpOrderEntryService;

    
    @GetMapping()
    @PreAuthorize("hasAuthority('rp:entry:entry')")
    public String orderEntry(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "rp/entry/entry";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('rp:entry:add')")
    public String add() {
        return "rp/entry/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('rp:entry:edit')")
    public String edit(@PathVariable Integer id, Model model) {
        RPOrderEntryDO orderEntry = rpOrderEntryService.getById(id);
        model.addAttribute("entry", orderEntry);
        return "rp/entry/edit";
    }

    @LogRecord(value = "财务单-保存", bizId = "#_ret['billNo']")
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('rp:entry:add')")
    public R save(@RequestBody @Validated RPOrderVO order) {
        rpOrderValidator.validateSave(order);
        RPOrderDO orderDO = rpOrderEntryService.save(order);
        return R.ok(MapUtil.of("billNo", orderDO.getBillNo()));
    }

    @ResponseBody
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('rp:order:order')")
    public R get(OrderDetailParam param) {
        //查询列表数据
        RPOrderVO orderVO = rpOrderEntryService.getOrderVO(param);
        return R.ok().put("order", orderVO);
    }
}
