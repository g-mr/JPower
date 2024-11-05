package top.jpower.core.redis.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 锁切面
 *
 * @author mr.g
 */
@Aspect
@RequiredArgsConstructor
public class LockAspect {

    private final LockHandler lockHandler;

    @Pointcut("@annotation(globalLock)")
    public void lockMethods(GlobalLock globalLock) {}

    @Around("lockMethods(globalLock)")
    public Object around(ProceedingJoinPoint joinPoint, GlobalLock globalLock) throws Throwable {
        return lockHandler.lock(globalLock, joinPoint::proceed);
    }

}
