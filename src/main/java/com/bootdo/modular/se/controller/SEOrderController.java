package com.bootdo.modular.se.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.param.SeOrderQryParam;
import com.bootdo.modular.se.service.SEOrderService;
import com.bootdo.modular.se.validator.SEOrderValidator;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购货订单
 *
 * @author yogiCai
 * @since 2018-02-18 16:50:26
 */
@Api(tags = "销售单")
@Controller
@RequestMapping("/se/order")
public class SEOrderController {
    @Resource
    private SEOrderValidator seOrderValidator;
    @Resource
    private SEOrderService seOrderService;

    @GetMapping()
    @RequiresPermissions("se:order:order")
    public String order() {
        return "se/order/order";
    }

    @ResponseBody
    @PostMapping(value = "/list")
    @RequiresPermissions("se:order:order")
    public PageJQ listP(@RequestBody SeOrderQryParam param) {
        return list(param);
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("se:order:order")
    public PageJQ list(SeOrderQryParam param) {
        //查询列表数据
        return seOrderService.page(param);
    }

    /**
     * 单据列表导出
     */
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("po:order:order")
    public void export(SeOrderQryParam param) {
        //查询列表数据
        List<SEOrderDO> orderList = seOrderService.pageList(PageFactory.defalultAllPage(), param).getRecords();
        PoiUtil.exportExcelWithStream("SEOrderResult.xls", SEOrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("销售单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("se:order:audit")
    public R audit(@RequestBody @Validated OrderAuditParam param) {
        seOrderValidator.validateAudit(param);
        seOrderService.audit(param);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("销售单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("se:order:remove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        seOrderValidator.validateRemove(billNos);
        seOrderService.batchRemove(billNos);
        return R.ok();
    }
}
