package top.jpower.core.feign;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;
import top.jpower.core.feign.annotation.DynamicFeignClient;

import java.util.Map;

public class AutoFeignClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    @Setter
    private ResourceLoader resourceLoader;
    @Setter
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 1. 获取 @AutoFeignClient 注解属性
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(DynamicFeignClient.class.getName());

        if (attributes == null) {
            return; // 不是 @AutoFeignClient 注解
        }

        // 2. 检查是否启用自动注册
        boolean autoRegister = (boolean) attributes.getOrDefault("autoRegister", true);
        if (!autoRegister) {
            return;
        }

        // 3. 检查注册条件
        String condition = (String) attributes.get("condition");
        if (StringUtils.hasText(condition)) {
            ConditionContext conditionContext = new SimpleConditionContext(
                    environment, resourceLoader, null);

            if (!new SpelExpressionParser().parseExpression(condition)
                    .getValue(conditionContext, Boolean.class)) {
                return; // 条件不满足，跳过注册
            }
        }

        // 4. 获取被注解的接口类
        Class<?> feignInterface;
        try {
            feignInterface = Class.forName(importingClassMetadata.getClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load Feign client interface", e);
        }

        // 5. 动态注册 Feign 客户端
        registerFeignClient(registry, feignInterface, attributes);
    }

    private void registerFeignClient(
            BeanDefinitionRegistry registry,
            Class<?> feignInterface,
            Map<String, Object> attributes) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(FeignClientFactoryBean.class);

        // 设置必需属性
        builder.addPropertyValue("type", feignInterface);
        builder.addPropertyValue("name", resolveAttribute(attributes, "name"));
        builder.addPropertyValue("url", resolveAttribute(attributes, "url"));
        builder.addPropertyValue("contextId", resolveAttribute(attributes, "contextId"));
        builder.addPropertyValue("path", resolveAttribute(attributes, "path"));

        // 设置配置类（如果有）
        Class<?>[] configurations = (Class<?>[]) attributes.get("configuration");
        if (configurations.length > 0) {
            builder.addPropertyValue("configuration", configurations[0]);
        }

        // 设置回退类（如果有）
        Class<?> fallback = (Class<?>) attributes.get("fallback");
        if (fallback != void.class) {
            builder.addPropertyValue("fallback", fallback);
        }

        // 设置回退工厂（如果有）
        Class<?> fallbackFactory = (Class<?>) attributes.get("fallbackFactory");
        if (fallbackFactory != void.class) {
            builder.addPropertyValue("fallbackFactory", fallbackFactory);
        }

        // 设置 primary 属性
        boolean primary = (boolean) attributes.getOrDefault("primary", true);
        builder.setPrimary(primary);

        // 注册为 Spring Bean
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, feignInterface);
        registry.registerBeanDefinition(
                feignInterface.getName(),
                beanDefinition
        );
    }

    private String resolveAttribute(Map<String, Object> attributes, String key) {
        String value = (String) attributes.get(key);
        if (value != null) {
            return environment.resolvePlaceholders(value);
        }
        return "";
    }

    // 简化的条件上下文实现
    private static class SimpleConditionContext implements ConditionContext {
        private final Environment environment;
        private final ResourceLoader resourceLoader;
        private final BeanDefinitionRegistry registry;

        public SimpleConditionContext(Environment environment, ResourceLoader resourceLoader,
                                      BeanDefinitionRegistry registry) {
            this.environment = environment;
            this.resourceLoader = resourceLoader;
            this.registry = registry;
        }

        @Override
        public BeanDefinitionRegistry getRegistry() {
            return registry;
        }

        @Override
        public ConfigurableListableBeanFactory getBeanFactory() {
            return null;
        }

        @Override
        public Environment getEnvironment() {
            return environment;
        }

        @Override
        public ResourceLoader getResourceLoader() {
            return resourceLoader;
        }

        @Override
        public ClassLoader getClassLoader() {
            return resourceLoader != null ? resourceLoader.getClassLoader() : null;
        }
    }
}