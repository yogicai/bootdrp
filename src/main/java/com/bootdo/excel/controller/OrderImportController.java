package com.bootdo.excel.controller;

import com.bootdo.common.utils.R;
import com.bootdo.excel.param.OrderImportParam;
import com.bootdo.excel.service.OrderImportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 购货订单
 *
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */

@Controller
@RequestMapping("/order")
public class OrderImportController {
    @Resource
    private OrderImportService importService;

    /**
     * 导入
     */
    @GetMapping("/import/excel")
    @RequiresPermissions("po:order:order")
    public String add() {
        return "se/order/import";
    }

    /**
     * 单据列表导出
     */
    @ResponseBody
    @PostMapping("/import/excel")
    @RequiresPermissions("po:order:order")
    public R importExcel(@Validated OrderImportParam orderImportParam) throws Exception {
        //查询列表数据
        importService.importExcel(orderImportParam);
        return R.ok();
    }
}
