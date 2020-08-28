package com.wlcb.jpower.module.base.exception.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ExceptionLogAspect
 * @Description TODO 未捕获异常的日志打印切面
 * @Author 郭丁志
 * @Date 2020-01-31 16:11
 * @Version 1.0
 */
//@Component
@Aspect
@Configuration
public class ExceptionLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogAspect.class);

    /**
     * @Author 郭丁志
     * @Description // 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点， 切入点为所有controller包下所有的方法
     * @Date 10:47 2020-08-28
     * @Param []
     * @return void
     **/
    @Pointcut("execution(* com.wlcb..*.controller..*.*(..))")
    public void ctrlPointCut(){}

    /**
     * @Author 郭丁志
     * @Description // 配置抛出异常后通知,使用在方法ctrlPointCut()上注册的切入点
     * @Date 10:47 2020-08-28
     * @Param [joinPoint, ex]
     * @return void
     **/
    @AfterThrowing(pointcut="ctrlPointCut()", throwing="ex")
    public void afterThrow(JoinPoint joinPoint, Throwable ex){
        logger.error("未捕获异常： ", ex);
    }
}
