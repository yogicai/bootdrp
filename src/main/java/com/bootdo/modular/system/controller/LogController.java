package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.param.SysLogParam;
import com.bootdo.modular.system.service.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author L
 */
@Tag(name = "系统日志")
@RequestMapping("/common/log")
@Controller
public class LogController {
    @Resource
    private LogService logService;


    @GetMapping()
    String log() {
        return "system/log/log";
    }

    @ResponseBody
    @GetMapping("/list")
    PageR list(SysLogParam param) {
        // 查询列表数据
        return logService.page(param);
    }

    @ResponseBody
    @PostMapping("/remove")
    R remove(Long id) {
        logService.removeById(id);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    R batchRemove(@RequestParam("ids[]") List<Integer> ids) {
        logService.removeBatchByIds(ids);
        return R.ok();
    }
}
