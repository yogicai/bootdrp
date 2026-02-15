package com.bootdo.modular.report.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtils;
import com.bootdo.modular.report.param.SReconEntryParam;
import com.bootdo.modular.report.param.SReconParam;
import com.bootdo.modular.report.param.SaleProductParam;
import com.bootdo.modular.report.result.SReconResult;
import com.bootdo.modular.report.result.SReconItem;
import com.bootdo.modular.report.result.SaleProductResult;
import com.bootdo.modular.report.service.ReportService;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public R<SReconResult> sRecon(@RequestBody SReconParam param) {
        return R.ok(reportService.sRecon(param));
    }

    @DataScope
    @ResponseBody
    @GetMapping(value = "/sRecon/export")
    @Operation(summary = "客户、供应商应收应付款-导出")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public void sReconExport(SReconParam param) {
        SReconResult result = reportService.sRecon(param);
        PoiUtils.exportExcelWithStream("SReconResult.xls", SReconItem.class, result.getItemList());
    }

    @GetMapping("/sRecon/entry")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public String sReconEntry() {
        return "report/sReconEntry";
    }

    @DataScope
    @ResponseBody
    @GetMapping(value = "/sRecon/entry/page")
    @Operation(summary = "客户、供应商应收应付款-明细")
    @PreAuthorize("hasAuthority('report:recon:recon')")
    public PageJQ sReconEntryPage(@Validated SReconEntryParam param) {
        return new PageJQ(reportService.sReconEntryPage(param));
    }

    /**
     * 商品销售统计报表
     */
    @GetMapping("/saleProduct")
    @PreAuthorize("hasAuthority('report:report:report')")
    public String saleProduct() {
        return "report/saleProduct";
    }

    @DataScope
    @ResponseBody
    @PostMapping(value = "/saleProduct")
    @Operation(summary = "销售统计报表")
    @PreAuthorize("hasAuthority('report:report:report')")
    public R<SaleProductResult> saleProduct(@RequestBody SaleProductParam param) {
        return R.ok(reportService.saleProduct(param));
    }
}
