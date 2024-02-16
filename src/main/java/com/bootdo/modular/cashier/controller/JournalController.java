package com.bootdo.modular.cashier.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.cashier.param.JournalGeneralParam;
import com.bootdo.modular.cashier.service.JournalService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 经营业绩
 *
 * @author yogiCai
 * @since 2018-07-14 22:31:58
 */
@Api(tags = "经营业绩")
@Controller
@RequestMapping("/cashier/journal")
public class JournalController {
    @Resource
    private JournalService journalService;

    @GetMapping()
    @RequiresPermissions("cashier:journal:journal")
    public String journal() {
        return "cashier/journal/journal";
    }

    @DataScope
    @ResponseBody
    @PostMapping("/general")
    @RequiresPermissions("cashier:journal:journal")
    public R general(@Validated JournalGeneralParam param) {
        return R.ok(journalService.general(param));
    }

    @DataScope
    @ResponseBody
    @GetMapping("/general/export")
    @RequiresPermissions("cashier:journal:journal")
    public void generalExport(@Validated JournalGeneralParam param) {
        journalService.export(param);
    }

}
