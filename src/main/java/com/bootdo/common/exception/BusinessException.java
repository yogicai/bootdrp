package com.bootdo.common.exception;

import java.text.MessageFormat;

/**
* @Author: yogiCai
* @Date: 2018-02-03 10:46:42
*/
public class BusinessException extends RuntimeException {

    private final String statusCode;

    private final String businessMessage;

    public BusinessException(String statusCode, String businessMessage) {
        super(getMessage(statusCode, businessMessage));
        this.statusCode = statusCode;
        this.businessMessage = businessMessage;
    }

    public BusinessException(String statusCode, String businessMessage, Exception e) {
        super(getMessage(statusCode, businessMessage), e);
        this.statusCode = statusCode;
        this.businessMessage = businessMessage;
    }

    private static String getMessage(String statusCode, String businessMessage) {
        return MessageFormat.format("StatusCode: {0}; BusinessMessage: {1}", statusCode, businessMessage);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }

}
