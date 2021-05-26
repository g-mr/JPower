package com.wlcb.jpower.module.common.redis;

import com.wlcb.jpower.module.common.utils.AspectBase;
import com.wlcb.jpower.module.common.utils.ProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.NEWLINE;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SPACE;

/**
 * @author mr.g
 * @date 2021-05-26 11:13
 */
@Slf4j
class RedisAdvice extends AspectBase {

    // 如果是getConnection方法，把返回结果进行代理包装
    Object interceptorRedisFactory(MethodInvocation invocation) throws Throwable {
        if (!log.isDebugEnabled()) {
            return invocation.proceed();
        }

        Object ret = invocation.proceed();
        String methodName = invocation.getMethod().getName();
        if (methodName.equals("getConnection")) {
            return ProxyUtils.getProxy(ret, this::interceptorRedis);
        }
        return ret;
    }

    private Object interceptorRedis(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String name = method.getName();
        if (name.equals("isPipelined") || name.equals("close")){
            return invocation.proceed();
        }

        Object target = invocation.getThis();
        Object[] args = invocation.getArguments();

        StringBuilder builder = new StringBuilder(NEWLINE);
        builder.append("===========START REDIS==============").append(NEWLINE);
        builder.append(SPACE).append("-->METHOD: ").append(name).append(NEWLINE);
        builder.append(SPACE).append("-->CLASS: ").append(target.getClass().getName()).append(NEWLINE);
        builder.append(SPACE).append("-->KEY: ").append(serial(args[0])).append(NEWLINE);
        builder.append(SPACE).append("-->PARAMS: ").append(serial(getParam(args))).append(NEWLINE);

        Object ret = null;
        long start = System.currentTimeMillis();
        try {
            ret = invocation.proceed();
            return ret;
        } catch (Exception exp) {
            throw exp;
        } finally {
            builder.append(SPACE).append("<--Time: ").append(System.currentTimeMillis()-start).append(" ms").append(NEWLINE);
            builder.append(SPACE).append("<--RESULT: ").append(serial(ret)).append(NEWLINE);
            builder.append("===========END REDIS==============");
            doLog(builder.toString());
        }
    }

    private Object[] getParam(Object[] args) {
        if (args.length > 1){
            Object[] o = new Object[args.length-1];
            for (int i = 1; i < args.length; i++) {
                o[i-1] = args[i];
            }
            return o;
        }
        return new Object[0];
    }
}