package com.wlcb.jpower.module.common.seata.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @ClassName WorkAspect
 * @Description 用于hystrix降级后无法触发事务，而进行的处理，使之可以正常的触发事务。
 *  TODO 暂时注释掉。因为偶尔会出现全局事务找不到的报错，暂无解决方案
 * @Author 郭丁志
 * @Date 2020-10-09 15:34
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class FallbackDegrade {

//    @Value("${seata.enabled:true}")
//    Boolean seataEnabled;
//    @Value("${jpower.hystrix.transaction:false}")
//    Boolean hystrixHransactionEnabled;
//
//    @Before("execution(* com.wlcb..*.feign..*Fallback.*(..))")
//    public void before(JoinPoint joinPoint) throws TransactionException {
//        if (hystrixHransactionEnabled){
//            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//            Method method = signature.getMethod();
//            log.info("{}方法已被降级", method.getName());
//            //需要先把本地事务回滚，不然seata会报找不到全局事务的错误
//            try {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            }catch (NoTransactionException e){
//                log.warn("未找到本地事务，无需回滚本地事务");
//            }
//            if (!StringUtils.isBlank(RootContext.getXID())) {
//                if (seataEnabled){
//                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//                }
//            }
//        }
//    }

}