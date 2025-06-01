package com.example.SunriseSunset.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**Aspect for logging method execution and errors in controllers and services.*/
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    /**Pointcut for all methods in controllers.*/

    @Pointcut("execution(* com.example.SunriseSunset.controller..*.*(..))")
    public void controllerMethods() {}
    /**Pointcut for all methods in services.*/

    @Pointcut("execution(* com.example.SunriseSunset.service..*.*(..))")
    public void serviceMethods() {}
    /**Logs method entry before execution.*/

    @Before("controllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {}", joinPoint.getSignature().toShortString());
    }
    /**Logs method exit with return value.*/

    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }
    /**Logs exceptions thrown by methods.*/

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in method: {} - {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }
}