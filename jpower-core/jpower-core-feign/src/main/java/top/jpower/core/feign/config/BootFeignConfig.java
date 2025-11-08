package top.jpower.core.feign.config;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import top.jpower.core.deploy.property.JpowerProperties;

import java.net.URI;

// TODO 这里不知道为什么BootFeignConfig会比JpowerDeployConfiguration之后执行，导致JpowerProperties不生效，需要解决
@AutoConfiguration
@ConditionalOnProperty(name = "jpower.server", havingValue = "BOOT")
public class BootFeignConfig {

    @Bean
    public FeignBuilderCustomizer feignBuilderCustomizer(JpowerProperties jpowerProperties) {
        return builder -> {
            // 在BOOT模式下，为所有Feign客户端添加请求拦截器来重写URL
            if (JpowerProperties.SERVER.BOOT.equals(jpowerProperties.getServer())) {
                builder.requestInterceptor(new BootModeRequestInterceptor(jpowerProperties.getPort()));
            }
        };
    }

    /**
     * BOOT模式请求拦截器 - 重写请求URL为localhost
     */
    private static class BootModeRequestInterceptor implements RequestInterceptor {
        private final String baseUrl;

        public BootModeRequestInterceptor(Integer port) {
            this.baseUrl = "http://localhost:" + port;
        }

        @Override
        public void apply(RequestTemplate template) {
            // 保存原始URL用于日志记录（可选）
            template.header("X-Original-Url", template.url());
            template.header("X-Feign-Mode", "BOOT");

            // 重写目标URL为localhost
            if (template.feignTarget() != null) {
                // 创建新的Target指向localhost
                Target<?> bootTarget = new Target.HardCodedTarget<>(
                        template.feignTarget().type(),
                        template.feignTarget().name(),
                        baseUrl
                );
                String path = URI.create(template.feignTarget().url()).getPath();
                template.target(StrUtil.appendIfMissing(baseUrl, "/") + path);
            }
        }
    }

}
