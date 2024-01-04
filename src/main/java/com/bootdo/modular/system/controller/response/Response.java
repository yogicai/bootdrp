package com.bootdo.modular.system.controller.response;

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
