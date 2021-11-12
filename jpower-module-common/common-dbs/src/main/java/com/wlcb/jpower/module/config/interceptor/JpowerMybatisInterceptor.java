package com.wlcb.jpower.module.config.interceptor;


import lombok.SneakyThrows;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis拦截器
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Intercepts(
    {
        @Signature(type = ResultSetHandler.class,method = "handleResultSets",args = {Statement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    }
)
public class JpowerMybatisInterceptor implements Interceptor {

    List<MybatisInterceptor> interceptors = new ArrayList<>();

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        Object target = invocation.getTarget();
        if (target instanceof Executor){
            final Executor executor = (Executor) target;
            // TODO: 2021/11/13 0013 这里得问题是在环绕拦截多个实现得情况下 如何处理返回值
            // TODO: 2021/11/13 0013 第二个问题是 这里如果执行多个 invocation.proceed() 是否会执行多次sql
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void addInterceptors(MybatisInterceptor mybatisInterceptor){
        interceptors.add(mybatisInterceptor);
    }

}
