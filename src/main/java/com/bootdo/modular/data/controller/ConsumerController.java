package com.bootdo.modular.data.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.base.param.BaseParam.edit;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.param.ConsumerQryParam;
import com.bootdo.modular.data.service.ConsumerService;
import com.bootdo.modular.data.validator.DataValidator;
import com.bootdo.modular.system.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户信息表
 *
 * @author yogiCai
 * @since 2018-02-16 16:30:26
 */
@Tag(name = "客户管理")
@Controller
@RequestMapping("/data/consumer")
public class ConsumerController extends BaseController {
    @Resource
    private DataValidator dataValidator;
    @Resource
    private ConsumerService consumerService;

    @GetMapping()
    public String consumer() {
        return "data/consumer/consumer";
    }

    @DataScope
    @ResponseBody
    @GetMapping("/list")
    @Operation(summary = "列表查询")
    public PageR list(ConsumerQryParam param) {
        return consumerService.page(param);
    }

    @DataScope
    @ResponseBody
    @GetMapping("/listJQ")
    @Operation(summary = "分页查询")
    public PageJQ listJQ(ConsumerQryParam param) {
        return consumerService.pageJQ(param);
    }

    @GetMapping("/add")
    public String add() {
        return "data/consumer/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        ConsumerDO consumer = consumerService.getById(id);
        model.addAttribute("consumer", consumer);
        return "data/consumer/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @Operation(summary = "保存")
    public R save(@Validated ConsumerDO consumer) {
        dataValidator.validateConsumer(consumer);
        consumerService.add(consumer);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    @Operation(summary = "修改")
    public R update(@Validated(edit.class) ConsumerDO consumer) {
        dataValidator.validateConsumer(consumer);
        consumerService.updateById(consumer);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('data:consumer:remove')")
    public R remove(Integer id) {
        consumerService.removeById(id);
        return R.ok();
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    @Operation(summary = "批量删除")
    @PreAuthorize("hasAuthority('data:consumer:batchRemove')")
    public R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        consumerService.removeBatchByIds(ids);
        return R.ok();
    }

}
