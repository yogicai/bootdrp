package com.bootdo.common.exception.biz.assertion;

import com.bootdo.common.exception.biz.AbstractExceptionEnum;
import lombok.Getter;

import java.text.MessageFormat;


/**
 * @author caiyz
 * @since 2022-03-03 10:09
 */
@Getter
public class BizServiceException extends RuntimeException {

    private final Integer code;

    private final String errorMessage;

    public BizServiceException(String message) {
        super(message);
        this.code = 500;
        this.errorMessage = message;
    }

    public BizServiceException(Integer code, String message) {
        super(message);
        this.code = code;
        this.errorMessage = message;
    }

    public BizServiceException(Integer code, String message, String errorMessage) {
        super(message);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public BizServiceException(Integer code, String message, String errorMessage, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public BizServiceException(AbstractExceptionEnum exception, Object... args) {
        super(MessageFormat.format(exception.getMessage(), args));
        this.code = exception.getCode();
        this.errorMessage = exception.getErrorMessage();
    }

}
