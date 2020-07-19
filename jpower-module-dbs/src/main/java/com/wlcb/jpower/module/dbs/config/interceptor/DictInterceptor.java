package com.wlcb.jpower.module.dbs.config.interceptor;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @ClassName Dict
 * @Description TODO 字典拦截器
 * @Author 郭丁志
 * @Date 2020-07-17 15:43
 * @Version 1.0
 */
@Intercepts({
        @Signature(type= Executor.class,method="query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})
})
public class DictInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        dealReturnValue(returnValue);
        return returnValue;
    }

    private void dealReturnValue(Object returnValue) {

        if(returnValue instanceof ArrayList<?>){
            List<?> list = (ArrayList<?>)returnValue;
            for(Object val: list){

            }
        }

    }

}
