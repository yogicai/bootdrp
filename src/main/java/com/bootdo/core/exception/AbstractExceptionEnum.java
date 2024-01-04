package com.bootdo.core.exception;

/**
 * 异常枚举格式规范
 *
 * @author caiyz
 * @since 2022-03-03 10:09
 */
public interface AbstractExceptionEnum {

    /**
     * 异常状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 后端异常日志提示信息
     *
     * @return 提示信息
     */
    String getMessage();

    /**
     * 前端异常提示信息
     *
     * @return 提示信息
     */
    String getErrorMessage();

}
