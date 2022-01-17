package com.bootdo.data.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.*;
import com.bootdo.data.domain.ProductCostDO;
import com.bootdo.data.domain.ProductDO;
import com.bootdo.data.service.ProductService;
import com.bootdo.data.validator.DataValidator;
import com.bootdo.report.service.WHReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品信息表
 * @Author: yogiCai
 * @Date: 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/product")
public class ProductController extends BaseController {
    @Autowired
    private DataValidator dataValidator;
	@Autowired
	private ProductService productService;
    @Autowired
    private WHReportService whReportService;

	
	@GetMapping()
	@RequiresPermissions("data:product:product")
	String Product(){
	    return "data/product/product";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("data:product:product")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ProductDO> productList = productService.list(query);
		int total = productService.count(query);
		PageUtils pageUtils = new PageUtils(productList, total);
		return pageUtils;
	}

    @ResponseBody
    @GetMapping("/listJQ")
    @RequiresPermissions("data:product:product")
    public PageJQUtils listJQ(@RequestParam Map<String, Object> params){
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ProductDO> productList = productService.list(query);
        Map<String, BigDecimal> balanceMap = new HashMap<>();
//        Map<String, BigDecimal> balanceMap = whReportService.queryPBalance(ImmutableMap.of("status", 1));
        //处理库存余额信息
        for (ProductDO productDO: productList) {
            productDO.setCostQty(MapUtils.getBigDecimal(balanceMap, productDO.getNo().toString()));
        }
        int total = productService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        PageJQUtils pageUtils = new PageJQUtils(productList, totalPage, query.getPage(), total);
        return pageUtils;
    }

	@GetMapping("/add")
	@RequiresPermissions("data:product:add")
	String add(){
	    return "data/product/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("data:product:edit")
	String edit(@PathVariable("id") Integer id,Model model){
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
	public R save( ProductDO product){
        dataValidator.validateProduct(product);
		if(productService.save(product)>0){
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
	public R update( ProductDO product){
        dataValidator.validateProduct(product);
		productService.update(product);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("data:product:remove")
	public R remove( Integer id){
		if(productService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("data:product:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		productService.batchRemove(ids);
		return R.ok();
	}

    @GetMapping("/productCost")
    @RequiresPermissions("data:product:product")
    String addHead(){
        return "data/product/productCost";
    }

	/**
	 * 库存全额查询页面-商品成本
	 */
	@GetMapping("/productCostB")
	@RequiresPermissions("data:product:product")
	String productCostB(){
		return "data/product/productCostB";
	}

    @ResponseBody
    @GetMapping("/listCost")
    @RequiresPermissions("data:product:product")
    public PageJQUtils listCost(@RequestParam Map<String, Object> params){
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<ProductCostDO> productList = productService.listCost(query);
        int total = productService.countCost(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        PageJQUtils pageUtils = new PageJQUtils(productList, totalPage, query.getPage(), total);
        return pageUtils;
    }
}
