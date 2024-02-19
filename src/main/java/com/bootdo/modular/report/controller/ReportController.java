package com.bootdo.modular.report.controller;

import cn.hutool.json.JSONUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.report.param.SReconParam;
import com.bootdo.modular.report.param.SaleProductParam;
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

/**
 * 报表
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@Api(tags = "报表")
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {
    @Resource
    private ReportService reportService;

    @GetMapping("/sRecon")
    @RequiresPermissions("report:recon:recon")
    public String sRecon(@RequestParam String type, Model model) {
        model.addAttribute("type", type);
        return "report/sRecon";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/sRecon")
    @ApiOperation(value = "客户、供应商应收应付款")
    @RequiresPermissions("report:recon:recon")
    public R sReconVC(@RequestBody SReconParam param) {
        return reportService.sRecon(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping(value = "/sRecon/export")
    @ApiOperation(value = "客户、供应商应收应付款-导出")
    @RequiresPermissions("report:recon:recon")
    public void sReconVCExport(SReconParam param) {
        R r = reportService.sRecon(param);
        List<SReconResult> result = JSONUtil.toList(JSONUtil.toJsonStr(r.get("result")), SReconResult.class);
        PoiUtil.exportExcelWithStream("SReconResult.xls", SReconResult.class, result);
    }

    /**
     * 商品销售统计报表
     */
    @GetMapping("/saleProduct")
    @RequiresPermissions("report:report:report")
    public String balance() {
        return "report/saleProduct";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/saleProduct")
    @ApiOperation(value = "销售统计报表")
    @RequiresPermissions("report:report:report")
    public R saleProduct(@RequestBody SaleProductParam param) {
        return reportService.saleProduct(param);
    }
}
