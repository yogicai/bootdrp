package com.bootdo.common.exception.biz.assertion;

import cn.hutool.json.JSONUtil;
import com.bootdo.common.exception.biz.AbstractExceptionEnum;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author caiyz
 * @since 2022-03-03 10:09
 */
public interface BizServiceAssert extends AbstractExceptionEnum, Assert {

    /**
     * 创建异常
     * @param args message占位符对应的参数列表
     * @return 业务异常
     */
    @Override
    default RuntimeException newException(Object... args) {
        String message = MessageFormat.format(this.getMessage(), convertArgs(args));
        String errorMessage = MessageFormat.format(this.getErrorMessage(), convertArgs(args));
        return new BizServiceException(this.getCode(), message, errorMessage);
    }

    /**
     * 创建异常
     * @param cause  待判断对象
     * @param args message占位符对应的参数列表
     * @return 业务异常
     */
    @Override
    default RuntimeException newException(Throwable cause, Object... args) {
        String message = MessageFormat.format(this.getMessage(), convertArgs(args));
        String errorMessage = MessageFormat.format(this.getErrorMessage(), convertArgs(args));
        return new BizServiceException(this.getCode(), message, errorMessage, cause);
    }

    /**
     * args参数转json处理
     * @param args message占位符对应的参数列表
     * @return 业务异常
     */
    default Object[] convertArgs(Object[] args) {
        Object[] argsT = Optional.ofNullable(args).orElse(new Object[]{});
        return Arrays.stream(argsT).map(JSONUtil::toJsonStr).toArray();
    }

}