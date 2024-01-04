package com.bootdo.modular.rp.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.Log;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.service.RPOrderService;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.rp.validator.RPOrderValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource
    private RPOrderValidator rpOrderValidator;
    @Resource
    private RPOrderService rpOrderService;

    @GetMapping()
    @RequiresPermissions("rp:order:order")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "rp/order/order";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rp:order:order")
    public PageJQ list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<RPOrderDO> orderList = rpOrderService.list(query);
        int total = rpOrderService.count(query);
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
        List<RPOrderDO> orderList = rpOrderService.list(query);
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
        rpOrderValidator.validateAudit(params);
        rpOrderService.audit(params);
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
        rpOrderValidator.validateRemove(billNos);
        rpOrderService.batchRemove(billNos);
        return R.ok();
    }
}
