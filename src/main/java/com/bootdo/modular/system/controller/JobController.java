package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.domain.TaskDO;
import com.bootdo.modular.system.param.SysTaskParam;
import com.bootdo.modular.system.service.JobService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @since 2017-09-26 20:53:48
 */
@Api(tags = "计划任务")
@Controller
@RequestMapping("/common/job")
public class JobController extends BaseController {
    @Resource
    private JobService jobService;

    @GetMapping()
    String taskScheduleJob() {
        return "system/job/job";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageR list(SysTaskParam param) {
        // 查询列表数据
        return jobService.page(param);
    }

    @GetMapping("/add")
    String add() {
        return "system/job/add";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        TaskDO job = jobService.getById(id);
        model.addAttribute("job", job);
        return "system/job/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        TaskDO taskScheduleJob = jobService.getById(id);
        return R.ok().put("taskScheduleJob", taskScheduleJob);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    public R save(TaskDO taskScheduleJob) {
        jobService.save(taskScheduleJob);
        return R.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @PostMapping("/update")
    public R update(TaskDO taskDO) {
        jobService.updateById(taskDO);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public R remove(Long id) {
        jobService.removeTask(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") List<Integer> ids) {
        jobService.batchRemoveTask(ids);
        return R.ok();
    }

    @PostMapping(value = "/changeJobStatus")
    @ResponseBody
    public R changeJobStatus(Long id, String cmd) {
        String label = "start".equals(cmd) ? "启动" : "停止";
        try {
            jobService.changeStatus(id, cmd);
            return R.ok("任务" + label + "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok("任务" + label + "失败");
    }

}
