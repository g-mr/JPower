package com.wlcb.jpower.module.aspectj;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.annotation.Log;
import com.wlcb.jpower.module.base.enums.BusinessStatus;
import com.wlcb.jpower.module.common.auth.UserInfo;
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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author 郭丁志
 * @Description //TODO 操作日志记录处理
 * @Date 17:38 2020-07-10
 **/
@Slf4j
@Aspect
@Component
public class LogAspect
{
    // 配置织入点
    @Pointcut("@annotation(com.wlcb.jpower.module.base.annotation.Log)")
    public void logPointCut(){
    }

    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint){
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        handleLog(joinPoint, e);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e)
    {
        try
        {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            UserInfo currentUser = LoginUserContext.get();

            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus. SUCCESS.ordinal());

            operLog.setOperUrl(WebUtil.getRequest().getRequestURI());
            if (currentUser != null){
                operLog.setOperIp(currentUser.getClientCode());
                operLog.setOperId(currentUser.getUserId());
                operLog.setOperName(currentUser.getUserName());
                operLog.setOperUserType(currentUser.getIsSysUser());
            }

            if (e != null){
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog);

            log.info("{}(id={})请求{}接口,参数={}；\n具体请求结果={}",operLog.getOperName(),operLog.getOperId(),operLog.getOperUrl(),operLog.getOperParam(),JSONObject.toJSONString(operLog));

            if (controllerLog.isSaveLog()){
                // TODO: 2020-07-10  保存到数据库 暂时未实现，后期添加
//            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
            }
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, SysOperLog operLog) throws Exception
    {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(SysOperLog operLog) throws Exception
    {
        Map<String, String[]> map = WebUtil.getRequest().getParameterMap();
        String params = JSONObject.toJSONString(map);
        operLog.setOperParam(params);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
