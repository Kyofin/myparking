package com.gec.myparking.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
/**
 * 日志切面类
 */
public class HttpAspect {

	public static final Logger logger = LoggerFactory.getLogger(HttpAspect.class);

	@Pointcut("execution(public * com.gec.myparking.controller.*.*(..))")
	public void log() {

	}

	@Before("log()")
	public void before(JoinPoint joinPoint) {
		ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		//url
		logger.info("url={}",request.getRequestURL());

		//请求方式
		logger.info("requestMethod={}",request.getMethod());

		//ip
		logger.info("ip={}",request.getRemoteAddr());

		//类方法
		logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"  .  "+joinPoint.getSignature().getName());

		//参数
		logger.info("args={}",joinPoint.getArgs());

	}

	@AfterReturning(returning = "object" ,pointcut = "log()")
	public void doAfterReturning(Object object) {
		logger.info("response={}",object);
	}
}
