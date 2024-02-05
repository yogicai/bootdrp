package com.bootdo.modular.engage.controller;

import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.param.ProductCostQryParam;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品成本管理
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Api(tags = "商品成本")
@Controller
@RequestMapping("/engage/product/cost")
public class ProductCostController extends BaseController {
    @Resource
    private ProductCostService productCostService;


    @GetMapping()
    @RequiresPermissions("engage:product:cost")
    public String cost() {
        return "engage/product/cost";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("engage:product:cost")
    public PageJQ list(ProductCostQryParam param) {
        //查询列表数据
        return productCostService.page(param);
    }

    /**
     * 成本导出
     */
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("engage:product:cost")
    public void export(ProductCostQryParam param) {
        //查询列表数据
        List<ProductCostDO> productList = productCostService.list(param);
        PoiUtil.exportExcelWithStream("ProductCostResult.xls", ProductCostDO.class, productList);
    }

    /**
     * 成本调整
     */
    @GetMapping("/adjust/{id}")
    @RequiresPermissions("engage:product:cost")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ProductCostDO productCost = productCostService.getById(id);
        model.addAttribute("productCost", productCost);
        return "engage/product/costAdjust";
    }

    /**
     * 成本调整
     */
    @ResponseBody
    @PostMapping("/adjust")
    @RequiresPermissions("engage:product:cost")
    public R update(ProductCostDO productCost) {
        productCostService.adjust(productCost);
        return R.ok();
    }

    /**
     * 商品成本变更明细
     */
    @GetMapping("/productCostB")
    public String productCostB() {
        return "engage/product/productCostB";
    }

}
