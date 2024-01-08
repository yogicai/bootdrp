package com.bootdo.modular.engage.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.modular.engage.result.BalanceResult;
import com.bootdo.modular.engage.service.ProductBalanceService;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.modular.wh.result.WHProductInfo;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.engage.param.BalanceAdjustParam;
import com.bootdo.modular.engage.result.EntryBalanceResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */

@Controller
@RequestMapping("/engage/product")
public class ProductBalanceController extends BaseController {
    @Resource
    private ProductBalanceService productBalanceService;

    /**
     * 库存余量查询-左侧菜单
     */
    @GetMapping("/balance")
    @RequiresPermissions("engage:product:balance")
    public String bBalance() {
        return "engage/product/balance";
    }

    /**
     * 库存余量查询
     */
    @ResponseBody
    @PostMapping(value = "/balance/list")
    @RequiresPermissions("engage:product:balance")
    public R pBalance(@RequestBody Map<String, Object> params, Model model) {
        params.put("status", 1);
        BalanceResult result = productBalanceService.pBalance(params);
        return R.ok().put("result", result);
    }

    /**
     * 库存余量导出
     */
    @ResponseBody
    @GetMapping(value = "/balance/export")
    @RequiresPermissions("engage:product:balance")
    public void pBalanceExport(@RequestParam Map<String, Object> params, Model model) {
        params.put("status", 1);
        BalanceResult result = productBalanceService.pBalance(params);
        PoiUtil.exportExcelWithStream("ProductBalanceResult.xls", WHProductInfo.class, result.getProductInfoList());
    }

    /**
     * 库存余量查询-左侧菜单
     */
    @GetMapping("/balanceEntry")
    @RequiresPermissions("engage:product:balance")
    public String bBalanceEntry(@RequestParam Map<String, Object> params, Model model) {
        return "engage/product/balanceEntry";
    }

    /**
     * 库存余量查询
     */
    @ResponseBody
    @GetMapping(value = "/balanceEntry/list")
    @RequiresPermissions("engage:product:balance")
    public PageJQ pBalanceEntry(@RequestParam Map<String, Object> params, Model model) {
        params.put("status", 1);
        QueryJQ query = new QueryJQ(params);
        List<EntryBalanceResult> productList = productBalanceService.pBalanceEntry(query);
        Map<String, Object> map = productBalanceService.pBalanceEntryCountSum(query);
        int total = MapUtil.getInt(map, "totalCount", 0);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        return new PageJQ(productList, totalPage, query.getPage(), total, map);
    }


    /**
     * 库存余量调整
     */
    @ResponseBody
    @GetMapping(value = "/balance/adjust")
    @RequiresPermissions("engage:product:balance")
    public R pBalance(BalanceAdjustParam balanceAdjustParam) {
        Collection<String> result = productBalanceService.pBalanceAdjust(balanceAdjustParam);
        return R.ok().put("result", result);
    }
}
