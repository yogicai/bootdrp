package com.bootdo.modular.report.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.report.result.SReconResult;
import com.bootdo.modular.report.service.ReportService;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 报表
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Api(tags = "报表")
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {
    @Resource
    private ReportService reportService;

    /**
     * 客户 供应商 应收应付款统计页面
     */
    @GetMapping("/sRecon")
    @RequiresPermissions("report:recon:recon")
    public String sRecon(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("type", MapUtil.getStr(params, "type"));
        return "report/sRecon";
    }

    /**
     * 客户 供应商 应收应付款统计（statistics reconciliation）
     */
    @ResponseBody
    @PostMapping(value = "/sRecon")
    @ApiOperation(value = "客户、供应商应收应付款")
    @RequiresPermissions("report:recon:recon")
    public R sReconVC(@RequestBody Map<String, Object> params, Model model) {
        return reportService.sRecon(params);
    }

    /**
     * 客户 供应商 应收应付款统计（statistics reconciliation）
     */
    @ResponseBody
    @GetMapping(value = "/sRecon/export")
    @ApiOperation(value = "客户、供应商应收应付款-导出")
    @RequiresPermissions("report:recon:recon")
    public void sReconVCExport(@RequestParam Map<String, Object> params, Model model) {
        R r = reportService.sRecon(params);
        List<SReconResult> result = JSONUtil.toList(JSONUtil.toJsonStr(r.get("result")), SReconResult.class);
        PoiUtil.exportExcelWithStream("SReconResult.xls", SReconResult.class, result);
    }

    /**
     * 商品销售统计报表
     */
    @GetMapping("/saleProduct")
    @RequiresPermissions("report:report:report")
    public String balance(@RequestParam Map<String, Object> params, Model model) {
        return "report/saleProduct";
    }


    @ResponseBody
    @PostMapping(value = "/saleProduct")
    @ApiOperation(value = "商品销售统计报表")
    @RequiresPermissions("report:report:report")
    public R saleProduct(@RequestBody Map<String, Object> params, Model model) {
        return reportService.saleProduct(params);
    }
}
