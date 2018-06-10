package com.gec.myparking.controller;

import com.gec.myparking.execption.GirlExecption;
import com.gec.myparking.util.MyparkingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String defaultExceptionHandler(HttpServletRequest req, Exception e) {
		//判断是否自定义异常类
		if (e instanceof GirlExecption) {
			GirlExecption girlExecption = (GirlExecption) e;
			return MyparkingUtil.getJsonString(girlExecption.getCode(), girlExecption.getMessage());
		} else {
			logger.error("\n\t---【系统异常】 ---\n\tHost {} invokes \n\turl {} \n\tERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getMessage());
			return MyparkingUtil.getJsonString(-1,e.getMessage());
		}


	}
}
