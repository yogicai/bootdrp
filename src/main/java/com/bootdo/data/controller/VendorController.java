package com.bootdo.data.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.*;
import com.bootdo.data.domain.VendorDO;
import com.bootdo.data.service.VendorService;
import com.bootdo.data.validator.DataValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 供应商信息表
 *
 * @author yogiCai
 * @date 2018-02-16 16:30:26
 */
@Controller
@RequestMapping("/data/vendor")
public class VendorController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private VendorService vendorService;

    @GetMapping()
    @RequiresPermissions("data:vendor:vendor")
    public String vendor() {
        return "data/vendor/vendor";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("data:vendor:vendor")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<VendorDO> vendorList = vendorService.list(query);
        int total = vendorService.count(query);
        return new PageUtils(vendorList, total);
    }

    @ResponseBody
    @GetMapping("/listJQ")
    @RequiresPermissions("data:vendor:vendor")
    public PageJQUtils listJQ(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<VendorDO> productList = vendorService.list(query);
        int total = vendorService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQUtils(productList, totalPage, query.getPage(), total);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:vendor:add")
    public String add() {
        return "data/vendor/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:vendor:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        VendorDO vendor = vendorService.get(id);
        model.addAttribute("vendor", vendor);
        return "data/vendor/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:vendor:add")
    public R save(VendorDO vendor) {
        dataValidator.validateVendor(vendor);
        if (vendorService.save(vendor) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("data:vendor:edit")
    public R update(VendorDO vendor) {
        dataValidator.validateVendor(vendor);
        vendorService.update(vendor);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:vendor:remove")
    public R remove(Integer id) {
        if (vendorService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:vendor:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        vendorService.batchRemove(ids);
        return R.ok();
    }

}
