package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 自定义分页拦截器
 *
 * @author mr.g
 * @date 2021-04-23 11:29
 */
public class JpowerPaginationInterceptor extends PaginationInnerInterceptor {

    @Setter
    private QueryInterceptor[] queryInterceptor;

    @SneakyThrows
    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql){
        if (Fc.isNotEmpty(queryInterceptor)){
            for (QueryInterceptor interceptor : queryInterceptor) {
                interceptor.intercept(executor,ms,parameter,rowBounds,resultHandler,boundSql);
            }
        }
        return super.willDoQuery(executor,ms,parameter,rowBounds,resultHandler,boundSql);
    }


}
