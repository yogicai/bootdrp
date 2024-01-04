package com.bootdo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 *
 * @author L
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * endPoint 注册协议节点,并映射指定的URl
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp 协议的endpoint,并指定 SockJS协议。
        registry.addEndpoint("/endpointWisely").withSockJS();
    }

    /**
     * 配置消息代理(message broker)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //广播式应配置一个/topic 消息代理
        registry.enableSimpleBroker("/topic");
    }
}
