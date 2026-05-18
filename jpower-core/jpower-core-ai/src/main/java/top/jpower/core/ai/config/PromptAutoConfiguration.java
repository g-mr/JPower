package top.jpower.core.ai.config;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.ai.prompt.DefaultPromptProcessed;
import top.jpower.core.ai.prompt.MybatisFlexPromptProcessed;
import top.jpower.core.ai.prompt.NacosPromptProcessed;
import top.jpower.core.ai.prompt.PromptProcessed;
import top.jpower.core.ai.properties.AiProperties;

/**
 * 提示词处理自动配置
 * <p>根据类路径和配置项选择性注入 PromptProcessed 实现</p>
 * <ul>
 *   <li>自定义实现优先：用户已提供 PromptProcessed Bean 时不会创建</li>
 *   <li>依赖选择性注入：根据类路径自动选择 Nacos 或 MybatisFlex 实现</li>
 *   <li>多依赖冲突处理：通过 jpower.ai.prompt.location 配置项决定</li>
 * </ul>
 *
 * @author mr.g
 */
@Slf4j
@AutoConfiguration
@ConditionalOnMissingBean(PromptProcessed.class)
@AutoConfigureAfter(name = {
        "com.alibaba.cloud.nacos.NacosConfigAutoConfiguration",
        "com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration"
})
@EnableConfigurationProperties(AiProperties.class)
public class PromptAutoConfiguration {



    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "jpower.ai.prompt", name = "location", havingValue = "nacos")
    @ConditionalOnClass(NacosConfigAutoConfiguration.class)
    @ConditionalOnProperty(prefix = "spring.cloud.nacos.config", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class NacosPromptConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PromptProcessed nacosPromptProcessed(AiProperties aiProperties,
                                                    NacosConfigProperties nacosConfigProperties) {
            log.info("使用 Nacos 配置中心作为提示词存储");
            return new NacosPromptProcessed(aiProperties, nacosConfigProperties);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "jpower.ai.prompt", name = "location", havingValue = "mybatis_flex", matchIfMissing = true)
    @ConditionalOnClass(MybatisFlexAutoConfiguration.class)
    public static class MybatisFlexPromptConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PromptProcessed mybatisFlexPromptProcessed(AiProperties aiProperties) {
            log.info("使用 MybatisFlex 数据库作为提示词存储，表名：{}",
                    aiProperties.getPrompt().getDatabaseTable());
            return new MybatisFlexPromptProcessed(aiProperties);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public PromptProcessed defaultPromptProcessed() {
        log.info("使用默认的提示词处理");
        return new DefaultPromptProcessed();
    }

}
