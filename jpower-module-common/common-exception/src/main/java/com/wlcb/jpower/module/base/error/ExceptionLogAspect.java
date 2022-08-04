package com.wlcb.jpower.module.base.error;

import com.wlcb.jpower.module.common.utils.ExceptionUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 异常的日志打印切面
 *
 * @author mr.g
 **/
@Aspect
@Configuration
public class ExceptionLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogAspect.class);

    /**
     * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点， 切入点为所有controller包下所有的方法
     *
     * @author mr.g
     **/
    @Pointcut("execution(* com.wlcb..*.controller..*.*(..))")
    public void ctrlPointCut(){}

    /**
     * 配置抛出异常后通知,使用在方法ctrlPointCut()上注册的切入点
     *
     * @author mr.g
     * @param joinPoint 切入点
     * @param ex 异常信息
     * @return void
     **/
    @AfterThrowing(pointcut="ctrlPointCut()", throwing="ex")
    public void afterThrow(JoinPoint joinPoint, Throwable ex){
        String methodName = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        logger.error("未捕获异常=>{}请求方法：{}, 异常信息：{}", StringPool.NEWLINE , methodName , ExceptionUtil.getStackTraceAsString(ex));
    }
}
