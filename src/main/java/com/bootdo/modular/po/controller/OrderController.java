package com.bootdo.modular.po.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.po.param.OrderQryParam;
import com.bootdo.modular.po.service.OrderService;
import com.bootdo.modular.po.validator.OrderValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 购货订单
 *
 * @author yogiCai
 * @since 2017-11-28 21:30:03
 */
@Api(tags = "采购单")
@Controller
@RequestMapping("/po/order")
public class OrderController extends BaseController {
    @Resource
    private OrderValidator orderValidator;
    @Resource
    private OrderService orderService;

    @GetMapping()
    @RequiresPermissions("po:order:order")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "po/order/order";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/list")
    @RequiresPermissions("se:order:order")
    public PageJQ listP(@RequestBody OrderQryParam param) {
        return list(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("po:order:order")
    public PageJQ list(OrderQryParam param) {
        return orderService.page(param);
    }

    /**
     * 单据列表导出
     */
    @DataScope
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("po:order:order")
    public void export(OrderQryParam param) {
        //查询列表数据
        List<OrderDO> orderList = orderService.pageList(PageFactory.defalultAllPage(), param).getRecords();
        PoiUtil.exportExcelWithStream("POOrderResult.xls", OrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("采购单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("po:order:audit")
    public R audit(@RequestBody OrderAuditParam param) {
        orderValidator.validateAudit(param);
        orderService.audit(param);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("采购单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("po:order:batchRemove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        orderValidator.validateRemove(billNos);
        orderService.batchRemove(billNos);
        return R.ok();
    }

}
