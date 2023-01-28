package com.bootdo.rp.controller;

import com.bootdo.common.annotation.Log;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.PageJQUtils;
import com.bootdo.common.utils.PoiUtil;
import com.bootdo.common.utils.QueryJQ;
import com.bootdo.common.utils.R;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.service.RPOrderService;
import com.bootdo.rp.validator.RPOrderValidator;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收付款单
 *
 * @author yogiCai
 * @date 2018-02-21 21:23:27
 */

@Controller
@RequestMapping("/rp/order")
public class RPOrderController extends BaseController {
    @Autowired
    private RPOrderValidator orderValidator;
    @Autowired
    private RPOrderService orderService;

    @GetMapping()
    @RequiresPermissions("rp:order:order")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtils.getString(params, "billType"));
        return "rp/order/order";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rp:order:order")
    public PageJQUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<RPOrderDO> orderList = orderService.list(query);
        int total = orderService.count(query);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        return new PageJQUtils(orderList, totalPage, query.getPage(), total);
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
        List<RPOrderDO> orderList = orderService.list(query);
        PoiUtil.exportExcelWithStream("RPOrderResult.xls", RPOrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("财务单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("rp:order:audit")
    public R audit(@RequestBody Map<String, Object> params) {
        orderValidator.validateAudit(params);
        orderService.audit(params);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("财务单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rp:order:remove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        orderValidator.validateRemove(billNos);
        orderService.batchRemove(billNos);
        return R.ok();
    }
}
