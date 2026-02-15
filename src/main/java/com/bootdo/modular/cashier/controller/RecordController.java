package com.bootdo.modular.cashier.controller;

import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.param.RecordImportParam;
import com.bootdo.modular.cashier.param.RecordQryParam;
import com.bootdo.modular.cashier.result.MultiSelect;
import com.bootdo.modular.cashier.service.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日记账
 *
 * @author yogiCai
 * @since 2018-07-14 22:31:58
 */
@Tag(name = "日记账明细")
@Controller
@RequestMapping("/cashier/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    @GetMapping()
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public String record() {
        return "cashier/record/record";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public PageJQ list(RecordQryParam param) {
        return recordService.page(param);
    }

    @ResponseBody
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public void export(RecordQryParam param) {
        recordService.export(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public String add() {
        return "cashier/record/add";
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public String edit(@PathVariable Integer id, Model model) {
        RecordDO recordDO = recordService.getById(id);
        model.addAttribute("record", recordDO);
        return "cashier/record/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public R<Boolean> update(RecordDO recordDO) {
        return R.ok(recordService.updateById(recordDO));
    }

    @LogRecord(value = "新增日记账")
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public R<Boolean> save(RecordDO recordDO) {
        return R.ok(recordService.save(recordDO.toManualRecord()));
    }

    @LogRecord(value = "删除日记账", bizId = "#ids")
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public R<Boolean> remove(@RequestParam("ids[]") List<Long> ids) {
        return R.ok(recordService.removeByIds(ids));
    }

    @LogRecord(value = "导入日记账")
    @PostMapping("/importCsv")
    @ResponseBody
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public R<Void> importCsv(RecordImportParam importParam) throws Exception {
        recordService.importCvs(importParam);
        return R.ok();
    }

    @GetMapping("/multiSelect")
    @ResponseBody
    @PreAuthorize("hasAuthority('cashier:record:record')")
    public MultiSelect multiSelect() {
        return recordService.multiSelect();
    }

}
