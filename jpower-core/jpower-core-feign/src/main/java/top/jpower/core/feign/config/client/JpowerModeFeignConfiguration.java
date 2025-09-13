package top.jpower.core.feign.config.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import top.jpower.core.deploy.property.JpowerProperties;

@AutoConfiguration
@RequiredArgsConstructor
public class JpowerModeFeignConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FeignClientProxyFactory feignClientProxyFactory(ApplicationContext applicationContext) {
        return new FeignClientProxyFactory(applicationContext);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public FeignClientFactoryBean feignClientFactoryBean(FeignClientProxyFactory feignClientProxyFactory,
                                                         JpowerProperties jpowerProperties) {

        return new ClientFeignClientFactoryBean(feignClientProxyFactory, jpowerProperties);
    }

    @RequiredArgsConstructor
    public static class ClientFeignClientFactoryBean extends FeignClientFactoryBean {

        private final FeignClientProxyFactory feignClientProxyFactory;
        private final JpowerProperties jpowerProperties;

        @Override
        public Object getObject() {
            if (jpowerProperties.getServer() == JpowerProperties.SERVER.BOOT) {
                // 单体模式：返回指向本地Controller的代理
                return feignClientProxyFactory.createFeignProxy(getObjectType(), getName());
            } else {
                // 微服务模式：使用默认实现
                return super.getObject();
            }
        }
    }

}
