package com.bootdo.modular.data.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.node.AsyncTree;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.service.CategoryService;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "类目管理")
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
    @ApiOperation(value = "列表查询")
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

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @RequiresPermissions("data:category:add")
    public R save(CategoryDO category) {
        if (categoryService.save(category) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    @RequiresPermissions("data:category:edit")
    public R update(CategoryDO category) {
        categoryService.update(category);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("data:category:remove")
    public R remove(Long categoryId) {
        if (categoryService.remove(categoryId) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:category:batchRemove")
    public R batchRemove(@RequestParam("ids[]") Long[] categoryIds) {
        categoryService.batchRemove(categoryIds);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "类目树菜单")
    public Tree<CategoryDO> tree(@RequestParam Map<String, Object> params) {
        return categoryService.getTree(params);
    }

    @GetMapping("/listTree/{types}")
    @ResponseBody
    @ApiOperation(value = "类目树下拉框数据")
    public Map<String, List<Tree<CategoryDO>>> listTree(@PathVariable("types") String types) {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("types", StringUtils.split(types, ","));
        return categoryService.listTree(map);
    }

    @GetMapping("/listTreeData/{types}")
    @ResponseBody
    @ApiOperation(value = "类目树关联数据下拉框数据(商品、供应商、客户、结算帐户)")
    public Map<String, List<Tree<Object>>> listTreeData(@PathVariable("types") String types) {
        // 查询列表数据
        return categoryService.listTreeData(ImmutableMap.of("types", StringUtils.split(types, ","), "status", 1));
    }


    @GetMapping("/productTree")
    @ResponseBody
    @ApiOperation(value = "类目树菜单")
    public List<AsyncTree<CategoryDO>> productTree(@RequestParam Map<String, Object> params) {
        String id = MapUtil.getStr(params, "id");
        if ("#".equals(id)) {
            return categoryService.getAsyncTree(params);
        } else {
            return categoryService.getAsyncTreeLeaf(params);
        }
    }
}
