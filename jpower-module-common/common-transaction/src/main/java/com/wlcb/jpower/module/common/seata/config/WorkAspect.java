package com.wlcb.jpower.module.common.seata.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @ClassName WorkAspect
 * @Description TODO 用于处理程序调用发生异常的时候由于异常被处理以后无法触发事务，而进行的处理，使之可以正常的触发事务。
 * @Author 郭丁志
 * @Date 2020-10-09 15:34
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class WorkAspect {

//    @AfterReturning("execution(* com.wlcb..*.feign..*Fallback.*(..))")
//    public void doRecoveryActions(JoinPoint joinPoint) throws TransactionException {
//        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//        Method method = signature.getMethod();
//        log.info("{}方法已被降级", method.getName());
//        if (!StringUtils.isBlank(RootContext.getXID())) {
//            //需要先把本地事务回滚，不然seata会报找不到全局事务的错误
//            try {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            }catch (NoTransactionException e){
//                log.warn("没有本地事务，无需回滚本地事务");
//            }
//            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//        }
//    }

}