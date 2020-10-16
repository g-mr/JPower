package com.wlcb.jpower.module.config;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.sql.Statement;

/**
 * @ClassName DictIntercept
 * @Description TODO 自动补全字典拦截器 （暂未实现）
 * @Author 郭丁志
 * @Date 2020/9/1 0001 21:20
 * @Version 1.0
 */
@Component
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class DictIntercept implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] objects =  invocation.getArgs();

        Object result = invocation.proceed();

        return result;
    }

}
