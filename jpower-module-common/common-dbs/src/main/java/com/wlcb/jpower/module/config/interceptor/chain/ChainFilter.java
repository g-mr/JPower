package com.wlcb.jpower.module.config.interceptor.chain;

import lombok.SneakyThrows;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Iterator;

/**
 * @Author mr.g
 * @Date 2021/11/14 0014 20:58
 */
public class ChainFilter {

    private final Iterator<MybatisInterceptor> interceptors;
    private final Invocation invocation;
    private final Executor executor;
    private MappedStatement ms;
    private Object parameter;
    private final boolean isUpdate;
    private RowBounds rowBounds;
    private ResultHandler resultHandler;
    private BoundSql boundSql;

    public ChainFilter(Iterator<MybatisInterceptor> interceptors,Invocation invocation){
        this.interceptors = interceptors;
        this.invocation = invocation;
        this.executor = (Executor) invocation.getTarget();

        Object[] args = invocation.getArgs();
        this.ms = (MappedStatement) args[0];
        this.parameter = args[1];
        this.isUpdate = args.length == 2;
        if (!isUpdate){
            rowBounds = (RowBounds) args[2];
            resultHandler = (ResultHandler) args[3];
            if (args.length == 4) {
                boundSql = ms.getBoundSql(parameter);
            } else {
                // 使用Executor的代理对象调用query[args[6]]
                boundSql = (BoundSql) args[5];
            }
        }
    }

    @SneakyThrows
    public Object proceed(){
        if (interceptors.hasNext()){
            if (isUpdate){
                return interceptors.next().aroundUpdate(this, executor, ms, parameter);
            }else if (ms.getSqlCommandType() == SqlCommandType.SELECT){
                return interceptors.next().aroundQuery(this, executor, ms, parameter, rowBounds, resultHandler, boundSql);
            }
        }
        return invocation.proceed();
    }

}
