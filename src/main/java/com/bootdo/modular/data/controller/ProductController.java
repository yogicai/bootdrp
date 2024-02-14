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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品信息表
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Api(tags = "商品管理")
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
    @ApiOperation(value = "列表查询")
    public PageR list(ProductQryParam param) {
        return productService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/listJQ")
    @ApiOperation(value = "分页查询")
    public PageJQ listJQ(ProductQryParam param) {
        return productService.pageJQ(param);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:product:add")
    public String add() {
        return "data/product/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:product:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ProductDO product = productService.getById(id);
        model.addAttribute("product", product);
        return "data/product/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @RequiresPermissions("data:product:add")
    public R save(@Validated ProductDO product) {
        dataValidator.validateProduct(product);
        productService.save(product);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(@Validated(edit.class) ProductDO product) {
        dataValidator.validateProduct(product);
        productService.updateById(product);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    public R remove(Integer id) {
        productService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:product:batchRemove")
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
    @ApiOperation(value = "商品成本")
    public PageJQ listCost(ProductCostQryParam param) {
        return productCostService.page(param);
    }
}
