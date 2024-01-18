package com.bootdo.modular.po.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.po.domain.OrderDO;
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
 * @date 2017-11-28 21:30:03
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

    @ResponseBody
    @PostMapping(value = "/list")
    @RequiresPermissions("se:order:order")
    public PageJQ listP(@RequestBody Map<String, Object> params) {
        return list(params);
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("po:order:order")
    public PageJQ list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<OrderDO> orderList = orderService.list(query);
        int total = orderService.count(query);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        return new PageJQ(orderList, totalPage, query.getPage(), total);
    }

    /**
     * 单据列表导出
     */
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("po:order:order")
    public void export(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params, false);
        List<OrderDO> orderList = orderService.list(query);
        PoiUtil.exportExcelWithStream("POOrderResult.xls", OrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("采购单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("po:order:audit")
    public R audit(@RequestBody Map<String, Object> params) {
        orderValidator.validateAudit(params);
        orderService.audit(params);
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
