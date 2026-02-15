package com.bootdo.modular.rp.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtils;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.param.RPOrderQryParam;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.rp.validator.RPOrderValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收付款单
 *
 * @author yogiCai
 * @since 2018-02-21 21:23:27
 */
@Tag(name = "收款、付款单")
@Controller
@RequestMapping("/rp/order")
public class RPOrderController extends BaseController {
    @Resource
    private RPOrderValidator rpOrderValidator;
    @Resource
    private RPOrderService rpOrderService;

    
    @GetMapping()
    @PreAuthorize("hasAuthority('rp:order:order')")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "rp/order/order";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('rp:order:order')")
    public PageJQ list(RPOrderQryParam param) {
        return rpOrderService.selectJoinGroupPage(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('po:order:order')")
    public void export(RPOrderQryParam param) {
        //查询列表数据
        List<RPOrderDO> orderList = rpOrderService.pageList(PageFactory.defalultAllPage(), param).getRecords();
        PoiUtils.exportExcelWithStream("RPOrderResult.xls", RPOrderDO.class, orderList);
    }

    @LogRecord(value = "'财务单-' + #param.auditStatus.remark1", bizId = "#param.billNos")
    @PostMapping("/audit")
    @ResponseBody
    @PreAuthorize("hasAuthority('rp:order:audit')")
    public R<Void> audit(@RequestBody @Validated OrderAuditParam param) {
        rpOrderValidator.validateAudit(param);
        rpOrderService.audit(param);
        return R.ok();
    }

    @LogRecord(value = "财务单-删除", bizId = "#billNos")
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('rp:order:remove')")
    public R<Void> remove(@RequestParam("billNos[]") List<String> billNos) {
        rpOrderValidator.validateRemove(billNos);
        rpOrderService.batchRemove(billNos);
        return R.ok();
    }
}
