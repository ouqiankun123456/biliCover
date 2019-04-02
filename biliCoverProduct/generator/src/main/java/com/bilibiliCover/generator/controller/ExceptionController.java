package com.bilibiliCover.generator.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bilibiliCover.generator.utils.R;

@RestController
@RestControllerAdvice
public class ExceptionController {

	
	/**
	 * 所有异常报错
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月12日
	 * @return R
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value= Exception.class)
	public R allExceptionHandler(HttpServletRequest request,
			Exception exception) {
		exception.printStackTrace();
		return R.error(exception.getMessage());
	}
}
