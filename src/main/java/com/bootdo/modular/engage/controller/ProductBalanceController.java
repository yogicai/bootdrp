package com.bootdo.modular.engage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.core.enums.CommonStatus;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtils;
import com.bootdo.modular.engage.param.BalanceAdjustParam;
import com.bootdo.modular.engage.param.BalanceQryParam;
import com.bootdo.modular.engage.param.EntryBalanceQryParam;
import com.bootdo.modular.engage.result.BalanceResult;
import com.bootdo.modular.engage.result.EntryBalanceResult;
import com.bootdo.modular.engage.result.EntryBalanceSumResult;
import com.bootdo.modular.engage.service.ProductBalanceService;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.modular.wh.result.WHProductInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@Tag(name = "商品库存")
@Controller
@RequestMapping("/engage/product")
public class ProductBalanceController extends BaseController {
    @Resource
    private ProductBalanceService productBalanceService;

    /**
     * 库存余量查询-左侧菜单
     */
    @GetMapping("/balance")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public String bBalance() {
        return "engage/product/balance";
    }

    /**
     * 库存余量查询
     */
    @ResponseBody
    @PostMapping(value = "/balance/list")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public R<BalanceResult> pBalance(@RequestBody BalanceQryParam param) {
        param.setStatus(CommonStatus.ENABLE.getValue());
        return R.ok(productBalanceService.pBalance(param));
    }

    /**
     * 库存余量导出
     */
    @ResponseBody
    @GetMapping(value = "/balance/export")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public void pBalanceExport(BalanceQryParam param) {
        param.setStatus(CommonStatus.ENABLE.getValue());
        BalanceResult result = productBalanceService.pBalance(param);
        PoiUtils.exportExcelWithStream("ProductBalanceResult.xls", WHProductInfo.class, result.getProductInfoList());
    }

    /**
     * 库存余量查询-左侧菜单
     */
    @GetMapping("/balanceEntry")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public String bBalanceEntry() {
        return "engage/product/balanceEntry";
    }

    /**
     * 库存余量查询
     */
    @ResponseBody
    @GetMapping(value = "/balanceEntry/list")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public PageJQ pBalanceEntry(EntryBalanceQryParam param) {
        param.setStatus(CommonStatus.ENABLE.getValue());
        Page<EntryBalanceResult> productList = productBalanceService.pBalanceEntry(param);
        EntryBalanceSumResult sumResult = productBalanceService.pBalanceEntryCountSum(param);
        return new PageJQ(productList, BeanUtil.beanToMap(sumResult));
    }


    /**
     * 库存余量调整
     */
    @ResponseBody
    @GetMapping(value = "/balance/adjust")
    @PreAuthorize("hasAuthority('engage:product:balance')")
    public R<Collection<String>> pBalance(BalanceAdjustParam balanceAdjustParam) {
        return R.ok(productBalanceService.pBalanceAdjust(balanceAdjustParam));
    }
}
