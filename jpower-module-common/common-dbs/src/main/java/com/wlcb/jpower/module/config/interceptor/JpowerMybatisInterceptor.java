package com.wlcb.jpower.module.config.interceptor;


import com.wlcb.jpower.module.config.interceptor.chain.ChainFilter;
import com.wlcb.jpower.module.config.interceptor.chain.MybatisInterceptor;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;
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
@AllArgsConstructor
public class JpowerMybatisInterceptor implements Interceptor {

    List<MybatisInterceptor> interceptors;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        Object target = invocation.getTarget();
        if (target instanceof Executor){
            return new ChainFilter(interceptors.iterator(),invocation).proceed();
        }else if (target instanceof ResultSetHandler){
            MetaObject metaObject = MetaObject.forObject(invocation.proceed(),new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
            return metaObject.getOriginalObject();
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

}
