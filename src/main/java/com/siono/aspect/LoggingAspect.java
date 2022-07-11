package com.siono.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

	@Pointcut("@annotation(Loggable)")
	public void logAllMethodCallsPointcut() {

	}
	
 
	/*
	 * Log every annotated method (when annotated with "Loggable")
	 */
	@Before("logAllMethodCallsPointcut()")
	public void deleteAdvice(JoinPoint joinPoint){
		log.info("deleting called with args: "+ Arrays.toString(joinPoint.getArgs()));
	}
	
	/*
	 * Log (debug) every method invoked in order service (when in debug mode)
	 */
	@Before("execution(* com.siono.service.OrderService.*(*))")
	public void debugOrdersAdvice(JoinPoint joinPoint){
		log.debug("debugOrdersAdvice - Before running loggingAdvice on method="+joinPoint.toString()+ " --- Arguments Passed=" + Arrays.toString(joinPoint.getArgs()));
	}
	
	/*
	 * Log every transaction before and after
	 */
	@Around("execution(* com.siono.service.impl.OrderServiceImpl.save(*))")
	public Object orderAdvice(ProceedingJoinPoint proceedingJoinPoint){
		log.info("orderAdvice before - Saving order with args: "+ Arrays.toString(proceedingJoinPoint.getArgs()));
		Object value = null;
		try {
			value = proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log.info("orderAdvice after - Order saved: "+value);
		return value;
	}
	
	@After("execution(* com.siono.service.OrderService.*())")
	public void advice4(JoinPoint joinPoint){
		//I could also be of use ??! Just uncomment me ... 
		//log.info("advice4 - After on method="+joinPoint.toString()+ " --- Arguments Passed=" + Arrays.toString(joinPoint.getArgs()));
	}
}

