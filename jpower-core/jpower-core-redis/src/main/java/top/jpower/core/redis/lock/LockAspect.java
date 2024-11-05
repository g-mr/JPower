package top.jpower.core.redis.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;

/**
 * 锁切面
 *
 * @author mr.g
 */
@Aspect
@RequiredArgsConstructor
public class LockAspect {

    private final LockHandler lockHandler;

    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();


    @Pointcut("@annotation(globalLock)")
    public void lockMethods(GlobalLock globalLock) {}

    @Around("lockMethods(globalLock)")
    public Object around(ProceedingJoinPoint joinPoint, GlobalLock globalLock) throws Throwable {
        LockDto lockDto = conver(globalLock, joinPoint);
        if (Fc.isNull(lockHandler)){
            return joinPoint.proceed();
        }
        return lockHandler.lock(lockDto, joinPoint::proceed);
    }

    private LockDto conver(GlobalLock globalLock, ProceedingJoinPoint joinPoint){
        return LockDto.builder()
                .name(generateKeyBySpEL(globalLock.name(), joinPoint))
                .type(globalLock.type())
                .waitTime(globalLock.waitTime())
                .leaveTime(globalLock.leaveTime())
                .unit(globalLock.unit())
                .build();
    }

    private String generateKeyBySpEL(String spString, ProceedingJoinPoint joinPoint) {
        if (Fc.isBlank(spString)){
            return StringPool.EMPTY;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        Expression expression = parser.parseExpression(spString);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }

}
