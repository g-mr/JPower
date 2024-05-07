package top.jpower.jpower.module.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.jpower.module.base.aspectj.OperateLogAspect;
import top.jpower.jpower.module.base.listener.ErrorLogListener;
import top.jpower.jpower.module.base.listener.OperateLogListener;

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
    public OperateLogListener operateLogListener(JpowerProperties jpowerProperties) {
        return new OperateLogListener(jpowerProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorLogListener")
    public ErrorLogListener errorLogListener(JpowerProperties jpowerProperties) {
        return new ErrorLogListener(jpowerProperties);
    }
}
