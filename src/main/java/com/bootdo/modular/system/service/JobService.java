package com.bootdo.modular.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.modular.system.dao.TaskDao;
import com.bootdo.modular.system.domain.TaskDO;
import com.bootdo.modular.system.param.SysTaskParam;
import com.bootdo.modular.timer.domain.ScheduleJob;
import com.bootdo.modular.timer.service.QuartzManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author L
 */
@Slf4j
@Service
public class JobService extends ServiceImpl<TaskDao, TaskDO> {
    @Resource
    private TaskDao taskDao;
    @Resource
    private QuartzManager quartzManager;


    public PageR page(SysTaskParam param) {
        return new PageR(this.pageList(PageFactory.defaultPage(), param));
    }

    public List<TaskDO> list(SysTaskParam param) {
        return this.pageList(PageFactory.defalultAllPage(), param).getRecords();
    }

    public Page<TaskDO> pageList(Page<TaskDO> page, SysTaskParam param) {
        LambdaQueryWrapper<TaskDO> queryWrapper = Wrappers.lambdaQuery(TaskDO.class)
                .ge(ObjectUtil.isNotEmpty(param.getStart()), TaskDO::getCreateDate, param.getStart())
                .le(ObjectUtil.isNotEmpty(param.getEnd()), TaskDO::getCreateDate, param.getEnd())
                .and(ObjectUtil.isNotEmpty(param.getSearchText()), query -> query.like(TaskDO::getJobName, param.getSearchText()));

        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeTask(Long id) {
        try {
            TaskDO taskDO = getById(id);
            quartzManager.deleteJob(BeanUtil.copyProperties(taskDO, ScheduleJob.class));
            taskDao.deleteById(id);
        } catch (SchedulerException e) {
            log.error("JobServiceImpl.remove error!", e);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveTask(List<Integer> ids) {
        ids.forEach(id -> {
            try {
                TaskDO taskDO = getById(id);
                quartzManager.deleteJob(BeanUtil.copyProperties(taskDO, ScheduleJob.class));
            } catch (SchedulerException e) {
                log.error("JobServiceImpl.batchRemove error!", e);
            }
        });
        taskDao.deleteByIds(ids);
    }

    public void initSchedule() {
        taskDao.selectList(Wrappers.query())
                .stream()
                .filter(job -> "1".equals(job.getJobStatus()))
                .forEach(scheduleJob -> {
                    ScheduleJob job = BeanUtil.copyProperties(scheduleJob, ScheduleJob.class);
                    quartzManager.addJob(job);
                });
    }

    public void changeStatus(Long jobId, String cmd) throws SchedulerException {
        TaskDO taskDO = this.getById(jobId);
        if (taskDO == null) {
            return;
        }
        if (Constant.STATUS_RUNNING_STOP.equals(cmd)) {
            quartzManager.deleteJob(BeanUtil.copyProperties(taskDO, ScheduleJob.class));
            taskDO.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
        } else if (Constant.STATUS_RUNNING_START.equals(cmd)) {
            taskDO.setJobStatus(ScheduleJob.STATUS_RUNNING);
            quartzManager.addJob(BeanUtil.copyProperties(taskDO, ScheduleJob.class));
        }
        this.updateById(taskDO);
    }

}
