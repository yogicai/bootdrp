package com.bootdo.modular.system.result;

import lombok.Data;

/**
 * @author L
 */
@Data
public class Response {

    private String responseMessage;

    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
