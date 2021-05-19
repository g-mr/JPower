package com.wlcb.jpower.module.config;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.config.properties.DemoProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.util.AntPathMatcher;

/**
 * @ClassName DemoInterceptor
 * @Description TODO 演示环境拦截器
 * @Author 郭丁志
 * @Date 2021/3/5 0005 22:31
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})})
public class DemoInterceptor implements Interceptor {

    private final DemoProperties properties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation){

        String path = Fc.notNull(WebUtil.getRequest()) ? WebUtil.getRequest().getServletPath() : null;

        // 匹配的接口进行放行
        if (Fc.notNull(path) && properties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))){
            return invocation.proceed();
        }

        log.warn("拦截到操作数据得SQL,演示环境不可操作数据");
        throw new UnsupportedOperationException("演示环境不支持操作！");
    }
}
