package com.wlcb.jpower.module.config.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.config.properties.DemoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
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
public class DemoInterceptor implements InnerInterceptor {

    private final DemoProperties properties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) {

        if (properties.isEnable()){
            String path = Fc.notNull(WebUtil.getRequest()) ? WebUtil.getRequest().getServletPath() : null;

            // 匹配的接口进行放行
            if (Fc.notNull(path) && properties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))){
                return true;
            }

            log.warn("拦截到操作数据得SQL,演示环境不可操作数据");
            return false;
        }
        return true;
    }
}
