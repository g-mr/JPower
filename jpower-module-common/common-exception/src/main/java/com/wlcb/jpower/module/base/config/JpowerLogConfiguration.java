package com.wlcb.jpower.module.base.config;

import com.wlcb.jpower.module.base.aspectj.OperateLogAspect;
import com.wlcb.jpower.module.base.feign.LogClient;
import com.wlcb.jpower.module.base.listener.ErrorLogListener;
import com.wlcb.jpower.module.base.listener.OperateLogListener;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.deploy.server.ServerInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 日志工具配置
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:16
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
public class JpowerLogConfiguration {

    @Resource
    private LogClient logService;

    @Bean
    public OperateLogAspect apiLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(name = "operateLogListener")
    public OperateLogListener operateLogListener(ServerInfo serverInfo,
                                                 JpowerProperties jpowerProperties) {
        return new OperateLogListener(logService, serverInfo, jpowerProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorLogListener")
    public ErrorLogListener errorLogListener(ServerInfo serverInfo,
                                               JpowerProperties jpowerProperties) {
        return new ErrorLogListener(logService, serverInfo, jpowerProperties);
    }
}
