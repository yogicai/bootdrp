package com.bootdo.modular.rp.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.param.PointQryParam;
import com.bootdo.modular.rp.service.PointEntryService;
import com.bootdo.modular.rp.validator.RPPointValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户积分
 *
 * @author yogiCai
 * @since 2018-03-06 23:17:49
 */
@Tag(name = "积分记录")
@Controller
@RequestMapping("/rp/point")
public class RPPointController {
    @Resource
    private RPPointValidator rpPointValidator;
    @Resource
    private PointEntryService pointEntryService;


    @GetMapping()
    @PreAuthorize("hasAuthority('rp:point:point')")
    public String point() {
        return "rp/point/point";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('rp:point:point')")
    public PageJQ list(PointQryParam param) {
        return pointEntryService.page(param);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('rp:point:add')")
    public String add() {
        return "rp/point/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('rp:point:edit')")
    public String edit(@PathVariable Integer id, Model model) {
        PointEntryDO pointEntry = pointEntryService.getById(id);
        model.addAttribute("pointEntry", pointEntry);
        return "rp/point/edit";
    }

    @LogRecord(value = "积分-保存", bizId = "#pointEntry.relateNo")
    @ResponseBody
    @RequestMapping({"/save", "/update"})
    @PreAuthorize("hasAuthority('rp:point:edit')")
    public R update(PointEntryDO pointEntry) {
        rpPointValidator.validateSave(pointEntry);
        pointEntryService.addOrUpdate(pointEntry);
        return R.ok();
    }

    @LogRecord(value = "积分-删除", bizId = "#ids")
    @PostMapping("/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('rp:point:remove')")
    public R remove(@RequestParam("ids[]") List<Integer> ids) {
        pointEntryService.removeByIds(ids);
        return R.ok();
    }
}
