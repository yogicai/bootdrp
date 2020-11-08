package com.bootdo.data.controller;

import com.bootdo.common.config.Constant;
import com.bootdo.common.domain.AsyncTree;
import com.bootdo.common.domain.Tree;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.data.domain.CategoryDO;
import com.bootdo.data.service.CategoryService;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类别管理
 * @Author: yogiCai
 * @Date: 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping()
	@RequiresPermissions("data:category:category")
	String Category(){
	    return "data/category/category";
	}
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions("data:category:category")
	public List<CategoryDO> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        List<CategoryDO> categoryList = categoryService.list(query);
        return categoryList;
	}
	
	@GetMapping("/add/{pId}")
	@RequiresPermissions("data:category:add")
	String add(@PathVariable("pId") Long pId, Model model){
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根类目");
        } else {
            model.addAttribute("pName", categoryService.get(pId).getName());
        }
        return  "data/category/add";
	}

	@GetMapping("/edit/{categoryId}")
	@RequiresPermissions("data:category:edit")
	String edit(@PathVariable("categoryId") Long categoryId,Model model){
        CategoryDO category = categoryService.get(categoryId);
        model.addAttribute("category", category);
        if(Constant.DEPT_ROOT_ID.equals(category.getParentId())) {
            model.addAttribute("pName", "根类目");
        }else {
            CategoryDO parCategory = categoryService.get(category.getParentId());
            model.addAttribute("pName", parCategory.getName());
        }
        return  "data/category/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("data:category:add")
	public R save( CategoryDO category){
		if(categoryService.save(category)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("data:category:edit")
	public R update( CategoryDO category){
		categoryService.update(category);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("data:category:remove")
	public R remove( Long categoryId){
		if(categoryService.remove(categoryId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("data:category:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] categoryIds){
		categoryService.batchRemove(categoryIds);
		return R.ok();
	}

	/**
	* 类目树菜单
	*/
    @GetMapping("/tree")
    @ResponseBody
    public Tree<CategoryDO> tree(@RequestParam Map<String, Object> params) {
        Tree<CategoryDO> tree = new Tree<CategoryDO>();
        tree = categoryService.getTree(params);
        return tree;
    }

    /**
     * 类目树下拉框数据
     */
    @GetMapping("/listTree/{types}")
    @ResponseBody
    public Map<String, List<Tree<CategoryDO>>> listTree(@PathVariable("types") String types) {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("types", StringUtils.split(types, ","));
        Map<String, List<Tree<CategoryDO>>> treeMap = categoryService.listTree(map);
        return treeMap;
    }

    /**
     * 类目树关联数据下拉框数据(商品、供应商、客户、结算帐户)
     */
    @GetMapping("/listTreeData/{types}")
    @ResponseBody
    public Map<String, List<Tree<Map>>> listTreeData(@PathVariable("types") String types) {
        // 查询列表数据
        Map<String, List<Tree<Map>>> treeMap = categoryService.listTreeData(ImmutableMap.of("types", StringUtils.split(types, ","), "status", 1));
        return treeMap;
    }


    /**
     * 类目树菜单
     */
    @GetMapping("/productTree")
    @ResponseBody
    public List<AsyncTree> productTree(@RequestParam Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        if ("#".equals(id)) {
            Tree<CategoryDO> tree = new Tree<CategoryDO>();
            return categoryService.getAsyncTree(params);
        } else {
            return categoryService.getAsyncTreeLeaf(params);
        }
    }
}
