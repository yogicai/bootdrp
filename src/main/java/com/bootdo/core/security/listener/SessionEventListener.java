package com.bootdo.core.security.listener;

import jakarta.servlet.http.HttpSessionEvent;
import lombok.Getter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 会话事件监听器
 * <p>
 * 作用：监听 HTTP 会话的创建和销毁事件，维护当前活跃会话数量统计
 * 继承自 HttpSessionEventPublisher，用于发布会话相关事件给 Spring Security
 *
 * @author L
 * @since 2026-01-08 11:11
 */
@Getter
@Component
public class SessionEventListener extends HttpSessionEventPublisher {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        super.sessionCreated(event);
        // 自定义会话创建逻辑
        sessionCount.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        super.sessionDestroyed(event);
        // 自定义会话销毁逻辑
        sessionCount.decrementAndGet();
    }
}
