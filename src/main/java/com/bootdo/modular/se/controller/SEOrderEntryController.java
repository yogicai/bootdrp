package com.bootdo.modular.se.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.se.service.SEOrderEntryService;
import com.bootdo.modular.se.validator.SEOrderValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @since 2018-02-18 16:50:26
 */
@Tag(name = "销售订单")
@Controller
@RequestMapping("/se/entry")
public class SEOrderEntryController {
    @Resource
    private SEOrderValidator seOrderValidator;
    @Resource
    private SEOrderEntryService seOrderEntryService;

    @GetMapping()
    @PreAuthorize("hasAuthority('se:entry:entry')")
    public String orderEntry() {
        return "se/entry/entry";
    }

    @LogRecord(value = "销售单-保存", bizId = "#_ret['billNo']")
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('se:entry:add')")
    public R save(@RequestBody @Validated SEOrderVO order) {
        seOrderValidator.validateSave(order);
        SEOrderDO orderDO = seOrderEntryService.save(order);
        return R.ok(MapUtil.of("billNo", orderDO.getBillNo()));
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('se:entry:add')")
    public String add() {
        return "se/entry/add";
    }

    @GetMapping("/addHead")
    @PreAuthorize("hasAuthority('se:entry:add')")
    public String addHead() {
        return "se/entry/addHead";
    }

    @ResponseBody
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('se:order:order')")
    public R get(@Validated OrderDetailParam param) {
        //查询列表数据
        SEOrderVO orderVO = seOrderEntryService.getOrderVO(param);
        return R.ok().put("order", orderVO);
    }
}
