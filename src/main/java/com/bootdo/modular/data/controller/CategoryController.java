package com.bootdo.modular.data.controller;

import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.node.Tree;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.CategoryDO;
import com.bootdo.modular.data.param.CategoryQryParam;
import com.bootdo.modular.data.service.CategoryService;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 类别管理
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Api(tags = "类目管理")
@Controller
@RequestMapping("/data/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping()
    public String category() {
        return "data/category/category";
    }

    @ResponseBody
    @GetMapping(value = "/list")
    @ApiOperation(value = "列表查询")
    public List<CategoryDO> list(CategoryQryParam param) {
        //查询列表数据
        return categoryService.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    @GetMapping("/add/{pId}")
    public String add(@PathVariable("pId") Long pId, Model model) {
        model.addAttribute("pId", pId);
        model.addAttribute("pName", Constant.DEPT_ROOT_ID.equals(pId) ? "根类目" : categoryService.getById(pId).getName());
        return "data/category/add";
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(@PathVariable("categoryId") Long categoryId, Model model) {
        CategoryDO category = categoryService.getById(categoryId);
        model.addAttribute("category", category);
        if (Constant.DEPT_ROOT_ID.equals(category.getParentId())) {
            model.addAttribute("pName", "根类目");
        } else {
            CategoryDO parCategory = categoryService.getById(category.getParentId());
            model.addAttribute("pName", parCategory.getName());
        }
        return "data/category/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public R save(CategoryDO category) {
        categoryService.save(category);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(CategoryDO category) {
        categoryService.updateById(category);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    public R remove(Long categoryId) {
        categoryService.removeById(categoryId);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    public R batchRemove(@RequestParam("ids[]") List<Integer> categoryIds) {
        categoryService.removeByIds(categoryIds);
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "类目树菜单")
    public Tree<CategoryDO> tree(CategoryQryParam param) {
        return categoryService.getTree(param);
    }

    @GetMapping("/listTree/{types}")
    @ResponseBody
    @ApiOperation(value = "缓存_类目树下拉框数据")
    public Map<String, List<Tree<CategoryDO>>> listTree(@PathVariable("types") String types) {
        // 查询列表数据
        return categoryService.listTree(CategoryQryParam.builder().type(types).build());
    }

    @GetMapping("/listTreeData/{types}")
    @ResponseBody
    @ApiOperation(value = "缓存_类目树关联数据下拉框数据(商品、供应商、客户、结算帐户、用户)")
    public Map<String, List<Tree<Object>>> listTreeData(@PathVariable("types") String types) {
        // 查询列表数据
        return categoryService.listTreeData(ImmutableMap.of("types", StrUtil.split(types, StrUtil.COMMA), "status", 1));
    }

}
