package com.bootdo.modular.cashier.controller;

import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.modular.cashier.param.ReconcileParam;
import com.bootdo.modular.cashier.service.ReconcileService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 收款对账
 *
 * @author L
 * @since 2025-03-18 22:55
 */
@Api(tags = "收款对账")
@Controller
@RequestMapping("/cashier/reconcile")
public class ReconcileController {
    @Resource
    private ReconcileService reconcileService;

    @GetMapping()
    @RequiresPermissions("cashier:reconcile:reconcile")
    public String journal() {
        return "cashier/reconcile/reconcile";
    }

    @ResponseBody
    @GetMapping("/page")
    @RequiresPermissions("cashier:reconcile:reconcile")
    public PageJQ page(@Validated ReconcileParam param) {
        return reconcileService.page(param);
    }

    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("cashier:reconcile:reconcile")
    public void export(@Validated ReconcileParam param) {
        reconcileService.export(param);
    }

}
