package com.bootdo.modular.data.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.data.validator.DataValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 仓库表
 *
 * @author yogiCai
 * @date 2018-02-18 16:23:32
 */

@Controller
@RequestMapping("/data/stock")
public class StockController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private StockService stockService;

    @GetMapping()
    @RequiresPermissions("data:stock:stock")
    public String stock() {
        return "data/stock/stock";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("data:stock:stock")
    public PageR list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<StockDO> stockList = stockService.list(query);
        int total = stockService.count(query);
        return new PageR(stockList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("data:stock:add")
    public String add() {
        return "data/stock/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("data:stock:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        StockDO stock = stockService.get(id);
        model.addAttribute("stock", stock);
        return "data/stock/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("data:stock:add")
    public R save(StockDO stock) {
        dataValidator.validateStock(stock);
        if (stockService.save(stock) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("data:stock:edit")
    public R update(StockDO stock) {
        dataValidator.validateStock(stock);
        stockService.update(stock);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("data:stock:remove")
    public R remove(Integer id) {
        if (stockService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("data:stock:remove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        stockService.batchRemove(ids);
        return R.ok();
    }

}
