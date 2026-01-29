package com.bootdo.modular.data.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.data.param.ProductQryParam;
import com.bootdo.modular.data.service.ProductService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.engage.param.ProductCostQryParam;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品信息表
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Tag(name = "商品管理")
@Controller
@RequestMapping("/data/product")
public class ProductController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private ProductService productService;
    @Resource
    private ProductCostService productCostService;


    @GetMapping()
    public String product() {
        return "data/product/product";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @Operation(summary = "列表查询")
    public PageR list(ProductQryParam param) {
        return productService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/listJQ")
    @Operation(summary = "分页查询")
    public PageJQ listJQ(ProductQryParam param) {
        return productService.pageJQ(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('data:product:add')")
    public String add() {
        return "data/product/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('data:product:edit')")
    public String edit(@PathVariable Integer id, Model model) {
        ProductDO product = productService.getById(id);
        model.addAttribute("product", product);
        return "data/product/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('data:product:add')")
    public R save(@Validated ProductDO product) {
        dataValidator.validateProduct(product);
        productService.add(product);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @Operation(summary = "修改")
    public R update(@Validated(edit.class) ProductDO product) {
        dataValidator.validateProduct(product);
        productService.updateById(product);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @Operation(summary = "删除")
    public R remove(Integer id) {
        productService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @Operation(summary = "批量删除")
    @PreAuthorize("hasAuthority('data:product:batchRemove')")
    public R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        productService.removeBatchByIds(ids);
        return R.ok();
    }

    /**
     * 报表-库存余量查询-商品成本查询（双击）
     */
    @GetMapping("/productCostB")
    public String productCostB() {
        return "data/product/productCostB";
    }

    @ResponseBody
    @GetMapping("/listCost")
    @Operation(summary = "商品成本")
    public PageJQ listCost(ProductCostQryParam param) {
        return productCostService.page(param);
    }
}
