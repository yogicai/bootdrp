package com.bootdo.modular.timer.tasks;

import com.bootdo.modular.system.controller.response.Response;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author L
 */
@Component
public class WelcomeJob implements Job {
    @Resource
    private SimpMessagingTemplate template;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        template.convertAndSend("/topic/getResponse", new Response("欢迎体验bootdo,这是一个任务计划，使用了websocket和quzrtz技术，可以在计划列表中取消，欢迎您加入qq群交流学习!"));
    }

}