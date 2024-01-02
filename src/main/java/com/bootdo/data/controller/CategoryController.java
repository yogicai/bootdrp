package com.bootdo.data.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.constants.Constant;
import com.bootdo.common.domain.AsyncTree;
import com.bootdo.common.domain.Tree;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.data.domain.CategoryDO;
import com.bootdo.data.service.CategoryService;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类别管理
 *
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping()
    @RequiresPermissions("data:category:category")
    public String category() {
        return "data/category/category";
    }

    @ResponseBody
    @GetMapping(value = "/list")
    @RequiresPermissions("data:category:category")
    public List<CategoryDO> list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return categoryService.list(query);
    }

    @GetMapping("/add/{pId}")
    @RequiresPermissions("data:category:add")
    public String add(@PathVariable("pId") Long pId, Model model) {
        model.addAttribute("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根类目");
        } else {
            model.addAttribute("pName", categoryService.get(pId).getName());
        }
        return "data/category/add";
    }

    @GetMapping("/edit/{categoryId}")
    @RequiresPermissions("data:category:edit")
    public String edit(@PathVariable("categoryId") Long categoryId, Model model) {
        CategoryDO category = categoryService.get(categoryId);
        model.addAttribute("category", category);
        if (Constant.DEPT_ROOT_ID.equals(category.getParentId())) {
            model.addAttribute("pName", "根类目");
        } else {
            CategoryDO parCategory = categoryService.get(category.getParentId());
            model.addAttribute("pName", parCategory.getName());
        }
        return "data/category/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:category:add")
    public R save(CategoryDO category) {
        if (categoryService.save(category) > 0) {
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
    public R update(CategoryDO category) {
        categoryService.update(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:category:remove")
    public R remove(Long categoryId) {
        if (categoryService.remove(categoryId) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:category:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] categoryIds) {
        categoryService.batchRemove(categoryIds);
        return R.ok();
    }

    /**
     * 类目树菜单
     */
    @GetMapping("/tree")
    @ResponseBody
    public Tree<CategoryDO> tree(@RequestParam Map<String, Object> params) {
        return categoryService.getTree(params);
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
        return categoryService.listTree(map);
    }

    /**
     * 类目树关联数据下拉框数据(商品、供应商、客户、结算帐户)
     */
    @GetMapping("/listTreeData/{types}")
    @ResponseBody
    public Map<String, List<Tree<Object>>> listTreeData(@PathVariable("types") String types) {
        // 查询列表数据
        return categoryService.listTreeData(ImmutableMap.of("types", StringUtils.split(types, ","), "status", 1));
    }


    /**
     * 类目树菜单
     */
    @GetMapping("/productTree")
    @ResponseBody
    public List<AsyncTree<CategoryDO>> productTree(@RequestParam Map<String, Object> params) {
        String id = MapUtil.getStr(params, "id");
        if ("#".equals(id)) {
            return categoryService.getAsyncTree(params);
        } else {
            return categoryService.getAsyncTreeLeaf(params);
        }
    }
}
