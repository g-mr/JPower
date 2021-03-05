package com.wlcb.jpower.module.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

/**
 * @ClassName DemoInterceptor
 * @Description TODO 演示环境拦截器
 * @Author 郭丁志
 * @Date 2021/3/5 0005 22:31
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})})
public class DemoInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation){
        log.warn("拦截到操作数据得SQL,演示环境不可操作数据");
        throw new UnsupportedOperationException("演示环境不支持操作！");
    }
}
