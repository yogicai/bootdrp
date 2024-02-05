package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.param.StockQryParam;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.data.validator.DataValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 仓库表
 *
 * @author yogiCai
 * @since 2018-02-18 16:23:32
 */
@Api(tags = "仓库管理")
@Controller
@RequestMapping("/data/stock")
public class StockController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private StockService stockService;

    @GetMapping()
    public String stock() {
        return "data/stock/stock";
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public PageR list(StockQryParam param) {
        //查询列表数据
        return stockService.page(param);
    }

    @GetMapping("/add")
    public String add() {
        return "data/stock/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        StockDO stock = stockService.getById(id);
        model.addAttribute("stock", stock);
        return "data/stock/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public R save(StockDO stock) {
        dataValidator.validateStock(stock);
        stockService.save(stock);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(StockDO stock) {
        dataValidator.validateStock(stock);
        stockService.updateById(stock);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("data:stock:remove")
    public R remove(Integer id) {
        stockService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("data:stock:remove")
    public R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        stockService.removeBatchByIds(ids);
        return R.ok();
    }

}
