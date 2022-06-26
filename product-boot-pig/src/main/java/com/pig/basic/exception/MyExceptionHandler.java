package com.pig.basic.exception;

import com.pig.basic.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {


	public static String getStackTrace(Exception e) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (PrintWriter pw = new PrintWriter(os)) {
			e.printStackTrace(pw);
		}
		e.printStackTrace();
		return os.toString();
	}

	@ExceptionHandler(value = MessageException.class)
	public Object exceptionHandler(MessageException e) {
		return CommonResult.failed(e.getMessage());
	}
	
	/**
	 * 全局异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public CommonResult exception(Exception e) {
		// log.error("保存全局异常信息 ex={}", e.getMessage(), e);
		log.error(getStackTrace(e));
		return CommonResult.failed(e,e.getMessage());
	}

	/**
	 * 全局服务异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public CommonResult serviceException(ServiceException e) {
		log.error("serviceException:",e);
		return CommonResult.failed(e.getReturnMessage());
	}

	/**
	 * 验证异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public CommonResult authenticationException(AuthenticationException e) {
		log.error("authenticationException:",e);
		return CommonResult.failed(e.getMessage());
	}
	/**
	 * 校验异常
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();
		List<String> errMsg = new ArrayList<>();
		for (FieldError errorMessage : allErrors) {
			errMsg.add(errorMessage.getField()+":"+errorMessage.getDefaultMessage() + ";");
		}
		return CommonResult.failed(StringUtils.join(errMsg.toArray()));
	}

	@ExceptionHandler(BusinessException.class)
	public CommonResult businessException(BusinessException e){
		return CommonResult.failed(e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public CommonResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		return CommonResult.failed("请求方式不支持");
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public CommonResult missingServletRequestParameterException(MissingServletRequestParameterException e){
		return CommonResult.failed("请求参数丢失,请排查:"+e.getParameterName());
	}
}
