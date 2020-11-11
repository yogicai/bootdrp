package com.bootdo.report.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.R;
import com.bootdo.report.service.ReportService;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 入库出库单
 * 
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
 
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{
	@Resource
	private ReportService reportService;

    /**
     * 客户 供应商 应收应付款统计页面
     */
	@GetMapping("/sRecon")
	@RequiresPermissions("report:recon:recon")
	String sRecon(@RequestParam Map<String, Object> params, Model model){
        model.addAttribute("type", MapUtils.getString(params, "type"));
        return "report/sRecon";
	}

    /**
     * 客户 供应商 应收就会款统计（statistics reconciliation）
     */
    @ResponseBody
    @RequestMapping(value = "/sRecon", method = RequestMethod.POST)
    @RequiresPermissions("report:recon:recon")
    R sReconVC(@RequestBody Map<String, Object> params, Model model) {
        return reportService.sRecon(params);
    }


    /**
     * main统计表格
     */
    @GetMapping("/mainTab")
    @RequiresPermissions("report:recon:recon")
    R mainTab(@RequestBody Map<String, Object> params, Model model) {
        return reportService.mainTab(params);
    }


    /**
     * 商品销售统计报表
     */
    @GetMapping("/saleProduct")
    @RequiresPermissions("wh:report:pBalance")
    String balance(@RequestParam Map<String, Object> params, Model model){
        return "report/saleProduct";
    }


    @ResponseBody
    @RequestMapping(value = "/saleProduct", method = RequestMethod.POST)
    @RequiresPermissions("report:report:report")
    R saleProduct(@RequestBody Map<String, Object> params, Model model) {
        return reportService.saleProduct(params);
    }
}
