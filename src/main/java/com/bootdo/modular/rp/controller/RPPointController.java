package com.bootdo.modular.rp.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.param.PointQryParam;
import com.bootdo.modular.rp.service.PointEntryService;
import com.bootdo.modular.rp.validator.RPPointValidator;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户积分
 *
 * @author yogiCai
 * @since 2018-03-06 23:17:49
 */
@Api(tags = "积分记录")
@Controller
@RequestMapping("/rp/point")
public class RPPointController {
    @Resource
    private RPPointValidator rpPointValidator;
    @Resource
    private PointEntryService pointEntryService;


    @GetMapping()
    @RequiresPermissions("rp:point:point")
    public String point() {
        return "rp/point/point";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rp:point:point")
    public PageJQ list(PointQryParam param) {
        //查询列表数据
        return pointEntryService.page(param);
    }

    @GetMapping("/add")
    @RequiresPermissions("rp:point:add")
    public String add() {
        return "rp/point/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("rp:point:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        PointEntryDO pointEntry = pointEntryService.getById(id);
        model.addAttribute("pointEntry", pointEntry);
        return "rp/point/edit";
    }

    @Log("积分保存、修改")
    @ResponseBody
    @RequestMapping({"/save", "/update"})
    @RequiresPermissions("rp:point:edit")
    public R update(PointEntryDO pointEntry) {
        rpPointValidator.validateSave(pointEntry);
        pointEntryService.addOrUpdate(pointEntry);
        return R.ok();
    }

    @Log("积分删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rp:point:remove")
    public R remove(@RequestParam("ids[]") List<Integer> ids) {
        pointEntryService.removeByIds(ids);
        return R.ok();
    }
}
