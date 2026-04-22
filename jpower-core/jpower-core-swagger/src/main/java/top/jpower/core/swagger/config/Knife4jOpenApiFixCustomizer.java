package top.jpower.core.swagger.config;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.github.xiaoymin.knife4j.core.conf.ExtensionsConstants;
import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jSetting;
import com.github.xiaoymin.knife4j.spring.extension.Knife4jOpenApiCustomizer;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 修复 knife4j 4.5.0 与 springdoc 2.8.x 的兼容性问题。
 * <p>
 * 原始 {@link Knife4jOpenApiCustomizer} 调用
 * {@code SpringDocConfigProperties.getGroupConfigs()} 时期望返回 {@code List}，
 * 但 springdoc 2.7+ 已将返回类型改为 {@code Set}，导致 {@link NoSuchMethodError}。
 * <p>
 * 本类继承 {@link Knife4jOpenApiCustomizer} 并重写 {@code customise()} 方法，
 * 以 {@code Set} 方式调用 {@code getGroupConfigs()}，解决兼容问题。
 * <p>
 * 继承父类使得 {@code @ConditionalOnMissingBean} 能够按类型检测到本 Bean，
 * 从而阻止 {@code Knife4jAutoConfiguration} 再创建原始的有问题的 Bean。
 */
public class Knife4jOpenApiFixCustomizer extends Knife4jOpenApiCustomizer {

    private final Knife4jProperties knife4jProperties;
    private final SpringDocConfigProperties properties;

    public Knife4jOpenApiFixCustomizer(Knife4jProperties knife4jProperties, SpringDocConfigProperties properties) {
        super(knife4jProperties, properties);
        this.knife4jProperties = knife4jProperties;
        this.properties = properties;
    }

    @Override
    public void customise(OpenAPI openApi) {
        if (knife4jProperties.isEnable()) {
            Knife4jSetting setting = knife4jProperties.getSetting();
            OpenApiExtensionResolver openApiExtensionResolver = new OpenApiExtensionResolver(setting, knife4jProperties.getDocuments());
            openApiExtensionResolver.start();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put(GlobalConstants.EXTENSION_OPEN_SETTING_NAME, setting);
            objectMap.put(GlobalConstants.EXTENSION_OPEN_MARKDOWN_NAME, openApiExtensionResolver.getMarkdownFiles());
            openApi.addExtension(GlobalConstants.EXTENSION_OPEN_API_NAME, objectMap);
            addOrderExtension(openApi);
        }
    }

    /**
     * 往 OpenAPI 内 tags 字段添加 x-order 属性（使用 Set 兼容 springdoc 2.8.x）
     */
    private void addOrderExtension(OpenAPI openApi) {
        // springdoc 2.7+ getGroupConfigs() 返回 Set 而非 List
        Set<SpringDocConfigProperties.GroupConfig> groupConfigs = properties.getGroupConfigs();
        if (CollectionUtils.isEmpty(groupConfigs)) {
            return;
        }
        Set<String> packagesToScan = groupConfigs.stream()
                .map(SpringDocConfigProperties.GroupConfig::getPackagesToScan)
                .filter(toScan -> !CollectionUtils.isEmpty(toScan))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(packagesToScan)) {
            return;
        }
        Set<Class<?>> classes = packagesToScan.stream()
                .map(packageToScan -> scanPackageByAnnotation(packageToScan, RestController.class))
                .flatMap(Set::stream)
                .filter(clazz -> clazz.isAnnotationPresent(ApiSupport.class))
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(classes)) {
            Map<String, Integer> tagOrderMap = new HashMap<>();
            classes.forEach(clazz -> {
                Tag tag = getTag(clazz);
                if (Objects.nonNull(tag)) {
                    ApiSupport apiSupport = clazz.getAnnotation(ApiSupport.class);
                    tagOrderMap.putIfAbsent(tag.name(), apiSupport.order());
                }
            });
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    if (tagOrderMap.containsKey(tag.getName())) {
                        tag.addExtension(ExtensionsConstants.EXTENSION_ORDER, tagOrderMap.get(tag.getName()));
                    }
                });
            }
        }
    }

    private Tag getTag(Class<?> clazz) {
        Tag tag = clazz.getAnnotation(Tag.class);
        if (Objects.isNull(tag)) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                for (Class<?> interfaceClazz : interfaces) {
                    Tag anno = interfaceClazz.getAnnotation(Tag.class);
                    if (Objects.nonNull(anno)) {
                        tag = anno;
                        break;
                    }
                }
            }
        }
        return tag;
    }

    private Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        Set<Class<?>> classes = new HashSet<>();
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(packageName)) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}
