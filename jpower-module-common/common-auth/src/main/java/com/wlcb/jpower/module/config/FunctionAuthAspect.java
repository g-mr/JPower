package com.wlcb.jpower.module.config;

import com.wlcb.jpower.module.annotation.Function;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import static com.wlcb.jpower.module.common.utils.constants.TokenConstant.HEADER_MENU;

/**
 * 权限切入点
 *
 * @author mr.g
 * @date 2022/10/6 16:53
 */
@Slf4j
@Aspect
@Configuration
public class FunctionAuthAspect {

    /**
     * 配置织入点
     * @Author mr.g
     **/
    @Pointcut("@annotation(com.wlcb.jpower.module.annotation.Function)")
    public void authPointCut(){ }

    /**
     * 处理前执行
     * @param joinPoint 切点
     */
    @SneakyThrows(GeneralSecurityException.class)
    @Before("authPointCut()")
    public void doBefore(JoinPoint joinPoint){

        // 开发环境不检测
        if (Fc.equalsValue(EnvBeanUtil.getProfile(), AppConstant.DEV_CODE)){
            return;
        }

        Function function = getAnnotation(joinPoint);
        if (Fc.isNull(function)){
            return;
        }

        HttpServletRequest request = WebUtil.getRequest();
        if (Fc.isNull(request)){
            return;
        }

        String menuCode = request.getHeader(HEADER_MENU);
        if (Fc.isBlank(menuCode)){
            log.warn("请求中没有带{}头，判定为非法访问！！！",HEADER_MENU);
            throw new GeneralSecurityException("非法访问！！！");
        }

        boolean is = Arrays.stream(function.menus()).filter(menu -> Fc.equalsValue(menu.client(), ShieldUtil.getClientCodeFromHeader())).anyMatch(menu -> Fc.equalsValue(menuCode,menu.menuCode()));
        if (!is){
            log.warn("请求头{}值不匹配，判定为非法访问！！！",HEADER_MENU);
            throw new GeneralSecurityException("非法访问！！！");
        }

    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Function getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (Fc.notNull(method)) {
            return AnnotationUtils.getAnnotation(method,Function.class);
        }
        return null;
    }

}
