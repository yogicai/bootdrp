package com.bootdo.listenner;

import com.bootdo.modular.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author L
 * @since 2023-12-13 13:28
 */
@Slf4j
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {
    @Resource
    private JobService jobService;

    @Override
    public void run(String... arg0) throws Exception {
        try {
            jobService.initSchedule();
        } catch (Exception e) {
            log.error("ScheduleJobInitListener.run error!", e);
        }

    }

}