package com.bootdo.modular.cashier.controller;

import com.bootdo.core.annotation.DataScope;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.cashier.param.JournalGeneralParam;
import com.bootdo.modular.cashier.result.JournalGeneralResult;
import com.bootdo.modular.cashier.service.JournalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 经营业绩
 *
 * @author yogiCai
 * @since 2018-07-14 22:31:58
 */
@Tag(name = "经营业绩")
@Controller
@RequestMapping("/cashier/journal")
public class JournalController {
    @Resource
    private JournalService journalService;

    @GetMapping()
    @PreAuthorize("hasAuthority('cashier:journal:journal')")
    public String journal() {
        return "cashier/journal/journal";
    }

    @DataScope
    @ResponseBody
    @PostMapping("/general")
    @PreAuthorize("hasAuthority('cashier:journal:journal')")
    public R<JournalGeneralResult> general(@Validated JournalGeneralParam param) {
        return R.ok(journalService.general(param));
    }

    @DataScope
    @ResponseBody
    @GetMapping("/general/export")
    @PreAuthorize("hasAuthority('cashier:journal:journal')")
    public void generalExport(@Validated JournalGeneralParam param) {
        journalService.export(param);
    }

}
