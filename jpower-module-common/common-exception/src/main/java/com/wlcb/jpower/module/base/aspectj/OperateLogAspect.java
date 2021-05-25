package com.wlcb.jpower.module.base.aspectj;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.base.annotation.OperateLog;
import com.wlcb.jpower.module.base.listener.OperateLogEvent;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.utils.FieldCompletionUtil;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.dbs.config.LoginUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author 郭丁志
 * @Description //TODO 操作日志记录处理
 * @Date 17:38 2020-07-10
 **/
@Slf4j
@Aspect
public class OperateLogAspect
{
    /**
     * 配置织入点
     * @Author mr.g
     * @param
     * @return void
     **/
    @Pointcut("@annotation(com.wlcb.jpower.module.base.annotation.OperateLog)")
    public void logPointCut(){
    }

    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(returning="rvt",pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint,Object rvt){
        handleLog(joinPoint, rvt, null);
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        handleLog(joinPoint, null, e);
    }

    protected void handleLog(final JoinPoint joinPoint, Object rvt, final Exception e){
        try {
            // 获得注解
            OperateLog controllerLog = getAnnotationLog(joinPoint);
            if (Fc.isNull(controllerLog)) {
                return;
            }

            OperateLogDto operLog = new OperateLogDto();
            StringBuilder builder = new StringBuilder(controllerLog.title());
            builder.append(" 请求==> ");
            // 获取当前的用户
            UserInfo currentUser = LoginUserContext.get();
            if (Fc.notNull(currentUser)){
                builder.append(currentUser.getUserName()).append("(id=").append(currentUser.getUserId()).append(")");
            }

            builder.append("请求").append(WebUtil.getRequest().getRequestURI()).append("接口;");
            builder.append("是否成功=").append(Fc.isNull(e)).append(";");

            log.info(builder.toString());

            if (controllerLog.isSaveLog()){

                operLog.setStatus(OperateLog.BusinessStatus.SUCCESS.ordinal());

                if (Fc.notNull(e)){
                    operLog.setStatus(OperateLog.BusinessStatus.FAIL.ordinal());
                    operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
                }

                // 设置方法名称
                String className = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                operLog.setMethodClass(className);
                operLog.setMethodName(methodName);
                operLog.setReturnContent(JSON.toJSONString(rvt));
                // 设置action动作
                operLog.setBusinessType(controllerLog.businessType().name());
                // 设置标题
                operLog.setTitle(controllerLog.value());
                // 处理设置注解上的参数
                if (controllerLog.isSaveRequestData()){
                    FieldCompletionUtil.requestInfo(operLog,WebUtil.getRequest());
                }
                FieldCompletionUtil.userInfo(operLog,currentUser);

                SpringUtil.publishEvent(new OperateLogEvent(operLog));
            }
        } catch (Exception exp) {
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OperateLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return Fc.getAnnotation(method,OperateLog.class);
        }
        return null;
    }
}
