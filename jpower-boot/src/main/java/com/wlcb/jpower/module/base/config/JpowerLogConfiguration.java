package com.wlcb.jpower.module.base.config;

import com.wlcb.jpower.module.base.aspectj.OperateLogAspect;
import com.wlcb.jpower.module.base.listener.ErrorLogListener;
import com.wlcb.jpower.module.base.listener.OperateLogListener;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.service.ErrorLogService;
import com.wlcb.jpower.service.OperateLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具配置
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:16
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
public class JpowerLogConfiguration {

    @Bean
    public OperateLogAspect apiLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(name = "operateLogListener")
    public OperateLogListener operateLogListener(OperateLogService logService,JpowerProperties jpowerProperties) {
        return new OperateLogListener(logService, jpowerProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorLogListener")
    public ErrorLogListener errorLogListener(ErrorLogService logService,JpowerProperties jpowerProperties) {
        return new ErrorLogListener(logService, jpowerProperties);
    }
}
