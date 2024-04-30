package top.jpower.jpower.module.base.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import top.jpower.core.utils.constants.StringPool;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.module.base.annotation.OperateLog;
import top.jpower.jpower.module.base.operate.OperateInfo;

import java.lang.reflect.Method;

/**
 * @Author 郭丁志
 * @Description //TODO 操作日志记录处理
 * @Date 17:38 2020-07-10
 **/
@Aspect
public class OperateLogAspect {

    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();


    /**
     * 配置织入点
     * @Author mr.g
     * @param
     * @return void
     **/
    @Pointcut("@annotation(top.jpower.jpower.module.base.annotation.OperateLog)")
    public void logPointCut(){
    }

    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(returning="rvt",pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint,Object rvt){
        // 获得注解
        OperateLog controllerLog = getAnnotationLog(joinPoint);
        if (Fc.isNull(controllerLog)) {
            return;
        }
        handleLog(copyOperateLog(controllerLog), joinPoint, rvt, null);
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        // 获得注解
        OperateLog controllerLog = getAnnotationLog(joinPoint);

        if (Fc.isNull(controllerLog) || !controllerLog.isErrorSaveLog()) {
            return;
        }
        handleLog(copyOperateLog(controllerLog), joinPoint, null, e);
    }

    protected void handleLog(final OperateInfo operateInfo, final JoinPoint joinPoint, Object rvt, final Exception e){
        final top.jpower.jpower.module.base.operate.OperateLog log = top.jpower.jpower.module.base.operate.OperateLog.SINGLETON(joinPoint.getTarget().getClass());
        operateInfo.recordId(generateKeyBySpEL(operateInfo.recordId(),joinPoint));
        operateInfo.content(generateKeyBySpEL(operateInfo.content(),joinPoint));
        log.info(operateInfo,joinPoint,rvt,e);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OperateLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return AnnotationUtils.getAnnotation(method,OperateLog.class);
        }
        return null;
    }

    public String generateKeyBySpEL(String spELString, JoinPoint joinPoint) {
        if (Fc.isBlank(spELString)){
            return StringPool.EMPTY;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        Expression expression = parser.parseExpression(spELString);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }

    private OperateInfo copyOperateLog(OperateLog operateLog){
        return new OperateInfo()
                .title(operateLog.title())
                .businessType(operateLog.businessType())
                .businessOther(operateLog.businessOther())
                .isSaveLog(operateLog.isSaveLog())
                .isSaveRequestData(operateLog.isSaveRequestData())
                .content(operateLog.content())
                .recordId(operateLog.recordId());
    }
}
