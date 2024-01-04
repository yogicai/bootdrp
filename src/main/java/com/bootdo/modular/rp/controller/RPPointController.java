package com.bootdo.modular.rp.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.service.RPPointService;
import com.bootdo.modular.rp.validator.RPPointValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客户积分
 *
 * @author yogiCai
 * @date 2018-03-06 23:17:49
 */
@Controller
@RequestMapping("/rp/point")
public class RPPointController {
    @Resource
    private RPPointValidator rpPointValidator;
    @Resource
    private RPPointService rpPointService;


    @GetMapping()
    @RequiresPermissions("rp:point:point")
    public String point(@RequestParam Map<String, Object> params, Model model) {
        return "rp/point/point";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rp:point:point")
    public PageJQ list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<PointEntryDO> orderList = rpPointService.list(query);
        int total = rpPointService.count(query);
        int totalPage = total / (query.getLimit() + 1) + 1;
        return new PageJQ(orderList, totalPage, query.getPage(), total);
    }

    @GetMapping("/add")
    @RequiresPermissions("rp:point:add")
    public String add() {
        return "rp/point/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("rp:point:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        PointEntryDO pointEntry = rpPointService.get(id);
        model.addAttribute("pointEntry", pointEntry);
        return "rp/point/edit";
    }

    /**
     * 保存
     */
    @Log("积分保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rp:point:add")
    public R save(PointEntryDO pointEntry) {
        rpPointValidator.validateSave(pointEntry);
        if (rpPointService.save(pointEntry) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @Log("积分修改")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("rp:point:edit")
    public R update(PointEntryDO pointEntry) {
        rpPointValidator.validateSave(pointEntry);
        rpPointService.update(pointEntry);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("积分删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rp:point:remove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        rpPointService.batchRemove(ids);
        return R.ok();
    }
}
