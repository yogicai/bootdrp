package com.bootdo.modular.po.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.param.OrderDetailParam;
import com.bootdo.modular.po.param.OrderVO;
import com.bootdo.modular.po.service.OrderEntryService;
import com.bootdo.modular.po.validator.OrderValidator;
import com.bootdo.modular.se.result.OrderSaveResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购货订单分录
 *
 * @author yogiCai
 * @since 2018-01-21 12:38:44
 */
@Tag(name = "采购订单")
@Controller
@RequestMapping("/po/entry")
public class OrderEntryController {
    @Resource
    private OrderValidator orderValidator;
    @Resource
    private OrderEntryService orderEntryService;

    @GetMapping()
    @PreAuthorize("hasAuthority('po:entry:entry')")
    public String orderEntry(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "po/entry/entry";
    }

    @LogRecord(value = "采购单-保存", bizId = "#_ret['billNo']")
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('po:entry:add')")
    public R<OrderSaveResult> save(@RequestBody @Validated OrderVO order) {
        orderValidator.validateSave(order);
        OrderDO orderDO = orderEntryService.save(order);
        return R.ok(new OrderSaveResult().setBillNo(orderDO.getBillNo()));
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('po:entry:add')")
    public String add() {
        return "po/entry/add";
    }


    @GetMapping("/addVendor")
    @PreAuthorize("hasAuthority('po:entry:add')")
    public String addHead() {
        return "po/entry/addVendor";
    }

    @ResponseBody
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('po:order:order')")
    public R<OrderVO> get(@Validated OrderDetailParam param) {
        return R.ok(orderEntryService.getOrderVO(param));
    }
}
