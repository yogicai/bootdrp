package com.bootdo.report.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.PoiUtil;
import com.bootdo.common.utils.R;
import com.bootdo.report.controller.response.WHPBalanceResult;
import com.bootdo.report.controller.response.WHPBalanceTotalResult;
import com.bootdo.report.service.WHReportService;
import com.bootdo.wh.controller.response.WHProductInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 入库出库单
 * 
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
 
@Controller
@RequestMapping("/report")
public class WHReportController extends BaseController{
	@Autowired
	private WHReportService whReportService;

    /**
     * 库存余量查询-左侧菜单
     */
	@GetMapping("/pBalance")
	@RequiresPermissions("wh:report:pBalance")
	String balance(@RequestParam Map<String, Object> params, Model model){
	    return "report/pBalance";
	}

    /**
     * 库存余量查询
     */
    @ResponseBody
    @RequestMapping(value = "/pBalance", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pBalance(@RequestBody Map<String, Object> params, Model model) {
        params.put("status", 1);
        WHPBalanceResult result = whReportService.pBalance(params);
        return R.ok().put("result", result);
    }

    /**
     * 库存余量导出
     */
    @ResponseBody
    @RequestMapping(value = "/pBalance/export", method = RequestMethod.GET)
    @RequiresPermissions("wh:report:pBalance")
    void pBalanceExport(@RequestParam Map<String, Object> params, Model model) {
        params.put("status", 1);
        WHPBalanceResult result = whReportService.pBalance(params);
        PoiUtil.exportExcelWithStream("WHProductInfoResult.xls", WHProductInfo.class, result.getProductInfoList());
    }

    /**
     * 库存余量 + 成本(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pBalanceTotal", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pBalanceTotal(@RequestBody Map<String, Object> params, Model model) {
        params.put("status", 1);
        WHPBalanceTotalResult result = whReportService.pBalanceTotal(params);
        return R.ok().put("result", result);
    }
}
