package com.bootdo.core.aspect;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.expression.ExpressionUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.bootdo.core.annotation.LogRecord;
import com.bootdo.core.utils.HttpServletUtil;
import com.bootdo.core.utils.SecurityUtils;
import com.bootdo.modular.system.dao.LogDao;
import com.bootdo.modular.system.domain.LogDO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Map;

/**
 * @author L
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Resource
    private LogDao logDao;

    @Pointcut("@annotation(com.bootdo.core.annotation.LogRecord)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point, result, time);

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result, long time) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogDO logDO = new LogDO();
            LogRecord logRecord = method.getAnnotation(LogRecord.class);
            // 请求的方法名
            logDO.setMethod(joinPoint.getTarget().getClass().getName() + "." + signature.getName());

            Object[] args = joinPoint.getArgs();
            // 请求的参数
            logDO.setParams(JSONUtil.toJsonStr(MapUtil.of("args", args[0])));

            // 解析 bizId、业务描述
            logDO.setBizId(evaluateExpression(logRecord.bizId(), method, args, result));
            logDO.setOperation(evaluateExpression(logRecord.value(), method, args, result));

            // 获取 request
            HttpServletRequest request = HttpServletUtil.getRequest();
            // 设置 IP地址
            logDO.setIp(JakartaServletUtil.getClientIP(request));
            // 用户名
            logDO.setUserId(SecurityUtils.getUserId());
            logDO.setUsername(SecurityUtils.getUserName());

            logDO.setTime((int) time);
            // 系统当前时间
            logDO.setGmtCreate(new Date());
            // 保存系统日志
            logDao.insert(logDO);

        } catch (Exception e) {
            log.error("保存系统日志失败！", e);
        }
    }

    public static String evaluateExpression(String expression, Method method, Object[] args, Object result) {
        if (expression == null || expression.isEmpty()) {
            return null;
        }
        // 构建变量映射
        Map<String, Object> variables = MapUtil.of("_ret", result);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            variables.put(parameters[i].getName(), args[i]);
        }
        try {
            return ExpressionUtil.eval(expression, variables).toString();
        } catch (Exception e) {
            return expression;
        }
    }

}
