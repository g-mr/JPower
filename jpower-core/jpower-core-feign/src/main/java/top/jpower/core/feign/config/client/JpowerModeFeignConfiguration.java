package top.jpower.core.feign.config.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.annotation.Bean;
import top.jpower.core.deploy.property.JpowerProperties;

@AutoConfiguration
@RequiredArgsConstructor
public class JpowerModeFeignConfiguration {

    private final FeignClientProxyFactory feignClientProxyFactory;
    private final JpowerProperties jpowerProperties;

    @Bean
    @ConditionalOnMissingBean
    public FeignClientFactoryBean feignClientFactoryBean() {
        return new ClientFeignClientFactoryBean();
    }

    public class ClientFeignClientFactoryBean extends FeignClientFactoryBean {

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
