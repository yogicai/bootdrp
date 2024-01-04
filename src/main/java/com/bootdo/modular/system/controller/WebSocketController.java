package com.bootdo.modular.system.controller;

import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.system.controller.request.Message;
import com.bootdo.modular.system.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author L
 */
@Slf4j
@Controller
public class WebSocketController {
    @Resource
    private SimpMessagingTemplate template;

    @MessageMapping("/welcome") // 浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse") // 服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public Response say(Message message) throws Exception {
        Thread.sleep(1000);
        return new Response("Welcome, " + message.getName() + "!");
    }

    @GetMapping("/test")
    String test() {
        return "test";
    }

    @RequestMapping("/welcome")
    @ResponseBody
    public R say02() {
        try {
            template.convertAndSend("/topic/getResponse", new Response("欢迎体验bootdo,这是一个任务计划，使用了websocket和quzrtz技术，可以在计划列表中取消，欢迎您加入qq群交流学习!"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return R.ok();
    }
}