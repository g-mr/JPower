package com.wlcb.jpower.module.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.Ordered;

/**
 * @author mr.g
 * @date 2021-04-23 11:33
 */
public interface QueryInterceptor extends Ordered {

    /**
     * 拦截器执行方法
     * @Author mr.g
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     **/
    void intercept(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql);

    @Override
    default int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    };
}
