package com.bootdo.modular.se.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtils;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.param.SeOrderQryParam;
import com.bootdo.modular.se.service.SEOrderService;
import com.bootdo.modular.se.validator.SEOrderValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购货订单
 *
 * @author yogiCai
 * @since 2018-02-18 16:50:26
 */
@Tag(name = "销售单")
@Controller
@RequestMapping("/se/order")
public class SEOrderController {
    @Resource
    private SEOrderValidator seOrderValidator;
    @Resource
    private SEOrderService seOrderService;

    
    @GetMapping()
    @PreAuthorize("hasAuthority('se:order:order')")
    public String order() {
        return "se/order/order";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('se:order:order')")
    public PageJQ list(SeOrderQryParam param) {
        return seOrderService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('po:order:order')")
    public void export(SeOrderQryParam param) {
        //查询列表数据
        List<SEOrderDO> orderList = seOrderService.pageList(PageFactory.defalultAllPage(), param).getRecords();
        PoiUtils.exportExcelWithStream("SEOrderResult.xls", SEOrderDO.class, orderList);
    }

    @LogRecord(value = "'销售单-' + #param.auditStatus.remark1", bizId = "#param.billNos")
    @PostMapping("/audit")
    @ResponseBody
    @PreAuthorize("hasAuthority('se:order:audit')")
    public R<Void> audit(@RequestBody @Validated OrderAuditParam param) {
        seOrderValidator.validateAudit(param);
        seOrderService.audit(param);
        return R.ok();
    }

    @LogRecord(value = "销售单-删除", bizId = "#billNos")
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('se:order:remove')")
    public R<Void> remove(@RequestParam("billNos[]") List<String> billNos) {
        seOrderValidator.validateRemove(billNos);
        seOrderService.batchRemove(billNos);
        return R.ok();
    }
}
