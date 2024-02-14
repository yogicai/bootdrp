package com.bootdo.modular.wh.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.Log;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.po.param.OrderAuditParam;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.param.WHOrderQryParam;
import com.bootdo.modular.wh.service.WHOrderService;
import com.bootdo.modular.wh.validator.WHOrderValidator;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@Api(tags = "出库、入库单")
@Controller
@RequestMapping("/wh/order")
public class WHOrderController extends BaseController {
    @Resource
    private WHOrderValidator whOrderValidator;
    @Resource
    private WHOrderService whOrderService;


    @GetMapping()
    @RequiresPermissions("wh:order:order")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "wh/order/order";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("wh:order:order")
    public PageJQ list(WHOrderQryParam param) {
        return whOrderService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("po:order:order")
    public void export(WHOrderQryParam param) {
        List<WHOrderDO> orderList = whOrderService.pageList(PageFactory.defalultAllPage(), param).getRecords();
        PoiUtil.exportExcelWithStream("WHOrderResult.xls", WHOrderDO.class, orderList);
    }

    @Log("库存单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("wh:order:audit")
    public R audit(@RequestBody OrderAuditParam param) {
        whOrderValidator.validateAudit(param);
        whOrderService.audit(param);
        return R.ok();
    }

    @Log("库存单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("wh:order:remove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        whOrderValidator.validateRemove(billNos);
        whOrderService.batchRemove(billNos);
        return R.ok();
    }

}
