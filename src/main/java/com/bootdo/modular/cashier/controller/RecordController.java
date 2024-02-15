package com.bootdo.modular.cashier.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.param.RecordImportParam;
import com.bootdo.modular.cashier.param.RecordQryParam;
import com.bootdo.modular.cashier.result.MultiSelect;
import com.bootdo.modular.cashier.service.RecordService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日记账
 *
 * @author yogiCai
 * @since 2018-07-14 22:31:58
 */
@Api(tags = "日记账明细")
@Controller
@RequestMapping("/cashier/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    @GetMapping()
    @RequiresPermissions("cashier:record:record")
    public String record() {
        return "cashier/record/record";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("cashier:record:record")
    public PageJQ list(RecordQryParam param) {
        return recordService.page(param);
    }

    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("cashier:record:record")
    public void export(RecordQryParam param) {
        recordService.export(param);
    }

    @GetMapping("/add")
    @RequiresPermissions("cashier:record:record")
    public String add() {
        return "cashier/record/add";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("cashier:record:record")
    public String edit(@PathVariable("id") Integer id, Model model) {
        RecordDO recordDO = recordService.getById(id);
        model.addAttribute("record", recordDO);
        return "cashier/record/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    @RequiresPermissions("cashier:record:record")
    public R update(RecordDO recordDO) {
        return R.ok(recordService.updateById(recordDO));
    }

    @Log("新增日记账")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("cashier:record:record")
    public R save(RecordDO recordDO) {
        return R.ok(recordService.save(recordDO.toManualRecord()));
    }

    @Log("删除日记账")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("cashier:record:record")
    public R remove(@RequestParam("ids[]") List<Long> ids) {
        return R.ok(recordService.removeByIds(ids));
    }

    @Log("导入日记账")
    @PostMapping("/importCsv")
    @ResponseBody
    @RequiresPermissions("cashier:record:record")
    public R importCsv(RecordImportParam importParam) throws Exception {
        recordService.importCvs(importParam);
        return R.ok();
    }

    @GetMapping("/multiSelect")
    @ResponseBody
    @RequiresPermissions("cashier:record:record")
    public MultiSelect multiSelect() {
        return recordService.multiSelect();
    }

}
