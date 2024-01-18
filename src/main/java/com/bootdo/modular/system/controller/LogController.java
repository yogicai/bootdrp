package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.request.Query;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.LogDO;
import com.bootdo.modular.system.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Api(tags = "系统日志")
@RequestMapping("/common/log")
@Controller
public class LogController {
    @Resource
    LogService logService;
    String prefix = "system/log";

    @GetMapping()
    String log() {
        return prefix + "/log";
    }

    @ResponseBody
    @GetMapping("/list")
    PageR list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<LogDO> logList = logService.list(query);
        int total = logService.count(query);
        return new PageR(logList, total);
    }

    @ResponseBody
    @PostMapping("/remove")
    R remove(Long id) {
        if (logService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    R batchRemove(@RequestParam("ids[]") Long[] ids) {
        int r = logService.batchRemove(ids);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }
}
