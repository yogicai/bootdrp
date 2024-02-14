package com.bootdo.modular.data.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.data.param.VendorQryParam;
import com.bootdo.modular.data.service.VendorService;
import com.bootdo.modular.data.validator.DataValidator;
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
 * 供应商信息表
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Api(tags = "供应商管理")
@Controller
@RequestMapping("/data/vendor")
public class VendorController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private VendorService vendorService;

    @GetMapping()
    public String vendor() {
        return "data/vendor/vendor";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public PageR list(VendorQryParam param) {
        return vendorService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/listJQ")
    @ApiOperation(value = "分页查询")
    public PageJQ listJQ(VendorQryParam param) {
        return vendorService.pageJQ(param);
    }

    @GetMapping("/add")
    public String add() {
        return "data/vendor/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        VendorDO vendor = vendorService.getById(id);
        model.addAttribute("vendor", vendor);
        return "data/vendor/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public R save(@Validated VendorDO vendor) {
        dataValidator.validateVendor(vendor);
        vendorService.save(vendor);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(@Validated(edit.class) VendorDO vendor) {
        dataValidator.validateVendor(vendor);
        vendorService.updateById(vendor);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("data:vendor:remove")
    public R remove(Integer id) {
        vendorService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:vendor:batchRemove")
    public R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        vendorService.removeByIds(ids);
        return R.ok();
    }

}
