package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.data.service.ProductService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品信息表
 *
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Api(tags = "商品管理")
@Controller
@RequestMapping("/data/product")
public class ProductController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private ProductService productService;


    @GetMapping()
    @RequiresPermissions("data:product:product")
    public String product() {
        return "data/product/product";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("data:product:product")
    public PageR list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<ProductDO> productList = productService.list(query);
        int total = productService.count(query);
        return new PageR(productList, total);
    }

    @ResponseBody
    @GetMapping("/listJQ")
    @RequiresPermissions("data:product:product")
    public PageJQ listJQ(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ProductDO> productList = productService.list(query);
        int total = productService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQ(productList, totalPage, query.getPage(), total);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:product:add")
    public String add() {
        return "data/product/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:product:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ProductDO product = productService.get(id);
        model.addAttribute("product", product);
        return "data/product/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:product:add")
    public R save(ProductDO product) {
        dataValidator.validateProduct(product);
        if (productService.save(product) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("data:product:edit")
    public R update(ProductDO product) {
        dataValidator.validateProduct(product);
        productService.update(product);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:product:remove")
    public R remove(Integer id) {
        if (productService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:product:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        productService.batchRemove(ids);
        return R.ok();
    }

    /**
     * 资料-商品管理-商品成本查询
     */
    @GetMapping("/productCost")
    @RequiresPermissions("data:product:product")
    public String productCost() {
        return "data/product/productCost";
    }

    /**
     * 报表-库存余量查询-商品成本查询（双击）
     */
    @GetMapping("/productCostB")
    @RequiresPermissions("data:product:product")
    public String productCostB() {
        return "data/product/productCostB";
    }

    @ResponseBody
    @GetMapping("/listCost")
    @RequiresPermissions("data:product:product")
    public PageJQ listCost(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ProductCostDO> productList = productService.listCost(query);
        int total = productService.countCost(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQ(productList, totalPage, query.getPage(), total);
    }
}
