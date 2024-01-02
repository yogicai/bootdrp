package com.bootdo.common.exception;

import cn.hutool.core.util.StrUtil;
import com.bootdo.common.exception.biz.assertion.BizServiceException;
import com.bootdo.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * 异常处理器
 *
 * @author L
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e) {
		log.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
		log.error(e.getMessage(), e);
		return R.error("没找找到页面");
	}

	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e) {
		log.error(e.getMessage(), e);
		return R.error("未授权");
	}

	@ExceptionHandler(BizServiceException.class)
	public R handleBizServiceException(BizServiceException e) {
		log.error(e.getMessage(), e);
		return R.error(e.getCode(), e.getErrorMessage());
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e) {
		log.error(e.getMessage(), e);
		return R.error("服务器错误，请联系管理员");
	}

	@ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
	public R handleInvalidArgument(BindException ex) {
		StringBuilder sb = new StringBuilder();
		//多个错误用逗号分隔
		List<ObjectError> allErrorInfos = ex.getAllErrors();
		for (ObjectError error : allErrorInfos) {
			if (error instanceof FieldError) {
				sb.append(StrUtil.COMMA).append(((FieldError) error).getField()).append(error.getDefaultMessage());
			} else {
				sb.append(StrUtil.COMMA).append(error.getDefaultMessage());
			}
		}
		return R.error(StrUtil.removePrefix(sb.toString(), StrUtil.COMMA));

	}
}
