package com.bootdo.modular.engage.controller;

import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.service.ProductCostService;
import com.bootdo.modular.engage.validator.EngageValidator;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品成本管理
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/engage/product/cost")
public class ProductCostController extends BaseController {
	@Resource
	private EngageValidator engageValidator;
	@Resource
	private ProductCostService productCostService;

	
	@GetMapping()
	@RequiresPermissions("engage:product:cost")
	public String cost(){
		return "engage/product/cost";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("engage:product:cost")
	public PageJQ list(@RequestParam Map<String, Object> params) {
		//查询列表数据
		QueryJQ query = new QueryJQ(params);
		List<ProductCostDO> productList = productCostService.costList(query);
		int total = productCostService.costCount(query);
		int totalPage = total / (query.getLimit() + 1) + 1;
		return new PageJQ(productList, totalPage, query.getPage(), total);
	}

	/**
	 * 成本导出
	 */
	@ResponseBody
	@GetMapping("/export")
	@RequiresPermissions("engage:product:cost")
	public void export(@RequestParam Map<String, Object> params) {
		//查询列表数据
		QueryJQ query = new QueryJQ(params, false);
		List<ProductCostDO> productList = productCostService.costList(query);
		PoiUtil.exportExcelWithStream("ProductCostResult.xls", ProductCostDO.class, productList);
	}

	/**
	 * 成本调整
	 */
	@GetMapping("/adjust/{id}")
	@RequiresPermissions("engage:product:cost")
	public String edit(@PathVariable("id") Integer id,Model model){
		ProductCostDO productCost = productCostService.get(id);
		model.addAttribute("productCost", productCost);
		return "engage/product/costAdjust";
	}
	
	/**
	 * 成本调整
	 */
	@ResponseBody
	@RequestMapping("/adjust")
	@RequiresPermissions("engage:product:cost")
	public R update( ProductCostDO productCost){
		engageValidator.validateProduct(productCost);
		productCostService.adjust(productCost);
		return R.ok();
	}
	

}
