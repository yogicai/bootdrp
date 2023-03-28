package com.bootdo.common.exception;

import com.bootdo.common.exception.biz.BizServiceException;
import com.bootdo.common.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常处理器
 * 
 */
@RestControllerAdvice
public class BDExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(BDException.class)
	public R handleBDException(BDException e) {
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e) {
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
	public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
		logger.error(e.getMessage(), e);
		return R.error("没找找到页面");
	}

	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e) {
		logger.error(e.getMessage(), e);
		return R.error("未授权");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error("服务器错误，请联系管理员");
	}

	@ExceptionHandler(BizServiceException.class)
	public R handleBizServiceException(BizServiceException e) {
		logger.error(e.getMessage(), e);
		return R.error(e.getCode(), e.getErrorMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public R handleException(BusinessException e) {
		logger.error(e.getMessage(), e);
		return R.error(e.getStatusCode(), e.getBusinessMessage());
	}

	@ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
	public ResponseEntity invalidArgument(Exception ex) {
		Map result = new HashMap<String, Object>();
		result.put("status_code", 500);
		StringBuffer sb = new StringBuffer();

		if (ex instanceof BindException) {
			List<FieldError> fieldErrors = ((BindException)ex).getFieldErrors();
			for (FieldError error : fieldErrors) {
				sb.append(error.getDefaultMessage());
			}
		} else if (ex instanceof MethodArgumentNotValidException) {
			List<FieldError> fieldErrors = ((MethodArgumentNotValidException)ex).getBindingResult().getFieldErrors();
			for (FieldError error : fieldErrors) {
				sb.append(error.getDefaultMessage());
			}
		}
		result.put("message", sb.toString());
		return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
