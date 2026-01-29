package com.bootdo.modular.report.controller;

import cn.hutool.json.JSONUtil;
import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtils;
import com.bootdo.modular.report.param.SReconParam;
import com.bootdo.modular.report.param.SaleProductParam;
import com.bootdo.modular.report.result.SReconResult;
import com.bootdo.modular.report.service.ReportService;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报表
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@Tag(name = "报表")
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {
    @Resource
    private ReportService reportService;

    @GetMapping("/sRecon")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public String sRecon(@RequestParam String type, Model model) {
        model.addAttribute("type", type);
        return "report/sRecon";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/sRecon")
    @Operation(summary = "客户、供应商应收应付款")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public R sReconVC(@RequestBody SReconParam param) {
        return reportService.sRecon(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping(value = "/sRecon/export")
    @Operation(summary = "客户、供应商应收应付款-导出")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public void sReconVCExport(SReconParam param) {
        R r = reportService.sRecon(param);
        List<SReconResult> result = JSONUtil.toList(JSONUtil.toJsonStr(r.get("result")), SReconResult.class);
        PoiUtils.exportExcelWithStream("SReconResult.xls", SReconResult.class, result);
    }

    /**
     * 商品销售统计报表
     */
    @GetMapping("/saleProduct")
    @PreAuthorize("hasAuthority('report:report:report')")
    public String balance() {
        return "report/saleProduct";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/saleProduct")
    @Operation(summary = "销售统计报表")
    @PreAuthorize("hasAuthority('report:report:report')")
    public R saleProduct(@RequestBody SaleProductParam param) {
        return reportService.saleProduct(param);
    }
}
