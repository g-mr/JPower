package top.jpower.core.redis.topic;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import top.jpower.core.util.utils.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mr.g
 * @date 2024-10-31 23:38
 * @description
 */
public class RedisTopicScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes redisTopicScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RedisTopicScan.class.getName()));
        if (redisTopicScanAttrs != null) {
            String name = StringUtil.lowerFirst(RedisTopicScannerConfigurer.class.getSimpleName());

            Set<String> basePackages = new HashSet<>();

            basePackages.addAll(Arrays.stream(redisTopicScanAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                    .collect(Collectors.toList()));

            basePackages.addAll(Arrays.stream(redisTopicScanAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                    .collect(Collectors.toList()));

            if (basePackages.isEmpty()) {
                basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
            }

            if (registry.isBeanNameInUse(name)){
                BeanDefinition beanDefinition = registry.getBeanDefinition(name);
                //noinspection unchecked,DataFlowIssue
                ((Set<String>) beanDefinition.getPropertyValues().getPropertyValue("basePackages").getValue()).addAll(basePackages);
            } else {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RedisTopicScannerConfigurer.class);
                builder.addPropertyValue("basePackages", basePackages);
                registry.registerBeanDefinition(name, builder.getBeanDefinition());
            }

        }
    }

}
