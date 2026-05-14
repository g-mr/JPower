package top.jpower.core.ai.config;

import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.observation.AdvisorObservationConvention;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.ai.client.ChatClientRequestBuilder;
import top.jpower.core.ai.client.ChatClients;
import top.jpower.core.ai.client.ChatModelRegistry;
import top.jpower.core.ai.enums.ChatModelType;
import top.jpower.core.ai.prompt.PromptProcessed;
import top.jpower.core.ai.properties.AiProperties;

import java.util.Map;

/**
 * ChatClient 多模型自动配置
 * <p>
 * 自动识别容器中所有 ChatModel Bean，注册到 ChatModelRegistry，
 * 并提供默认的 ChatClient 和 ChatClients 工厂 Bean。
 * <p>
 * 集成 Spring AI 原生的 ObservationRegistry、ChatClientBuilderConfigurer 等配置组件，
 * 确保用户自定义的观察、监控和顾问配置能够正确生效。
 * <p>
 * 当只有一个 ChatModel 时，自动将其作为默认模型；
 * 当存在多个 ChatModel 时，可通过 jpower.ai.primary 指定默认模型。
 *
 * @author mr.g
 */
@Slf4j
@AutoConfiguration
@ConditionalOnBean(ChatModel.class)
@EnableConfigurationProperties(AiProperties.class)
public class ChatClientAutoConfiguration {

    /**
     * 注册 ChatModel 注册表
     * <p>
     * 自动收集容器中所有 ChatModel Bean，根据类名推断模型类型并注册。
     * 根据配置或自动策略确定主模型。
     */
    @Bean
    @ConditionalOnMissingBean
    public ChatModelRegistry chatModelRegistry(AiProperties aiProperties,
                                               ObjectProvider<Map<String, ChatModel>> chatModelsProvider) {
        ChatModelRegistry registry = new ChatModelRegistry();

        Map<String, ChatModel> chatModels = chatModelsProvider.getIfAvailable();
        if (chatModels == null || chatModels.isEmpty()) {
            log.warn("未检测到任何 ChatModel Bean");
            return registry;
        }

        log.info("检测到 {} 个 ChatModel Bean", chatModels.size());

        // 注册所有 ChatModel
        ChatModel firstModel = null;
        for (Map.Entry<String, ChatModel> entry : chatModels.entrySet()) {
            String beanName = entry.getKey();
            ChatModel model = entry.getValue();

            if (firstModel == null) {
                firstModel = model;
            }

            ChatModelType type = ChatModelRegistry.inferType(model);
            if (type != null) {
                registry.register(type, model);
            } else {
                log.warn("无法推断 ChatModel Bean [{}] 的类型: {}，跳过注册", beanName, model.getClass().getName());
            }
        }

        // 确定主模型
        ChatModel primary = determinePrimary(aiProperties, registry, firstModel);
        if (primary != null) {
            registry.setPrimary(primary);
            log.info("主 ChatModel 已设置: {}", primary.getClass().getSimpleName());
        }

        return registry;
    }

    /**
     * 确定主模型的策略
     */
    private ChatModel determinePrimary(AiProperties properties, ChatModelRegistry registry, ChatModel firstModel) {
        // 1. 优先使用配置指定的主模型
        ChatModelType primaryType = properties.getPrimary();
        if (primaryType != null) {
            ChatModel configured = registry.getModel(primaryType);
            if (configured != null) {
                log.info("使用配置指定的主模型: {}", primaryType.getKey());
                return configured;
            }
            log.warn("配置指定的主模型 [{}] 未找到，将自动选择", primaryType.getKey());
        }

        // 2. 如果只有一个模型，直接使用
        if (registry.size() == 1) {
            return registry.getRegisteredTypes().iterator().next() != null
                    ? registry.getModel(registry.getRegisteredTypes().iterator().next())
                    : firstModel;
        }

        // 3. 多模型场景下按优先级选择默认模型
        ChatModelType[] priority = {
                ChatModelType.DEEPSEEK,
                ChatModelType.DASHSCOPE,
                ChatModelType.OPENAI,
                ChatModelType.AZURE_OPENAI,
                ChatModelType.OLLAMA,
                ChatModelType.ZHIPU
        };
        for (ChatModelType type : priority) {
            if (registry.contains(type)) {
                log.info("多模型场景，自动选择主模型: {}", type.getKey());
                return registry.getModel(type);
            }
        }

        // 4. 兜底使用第一个模型
        return firstModel;
    }


    @Bean
    @ConditionalOnMissingBean
    public ChatClientRequestBuilder chatClientRequestBuilder(PromptProcessed promptProcessed) {
        return new ChatClientRequestBuilder(promptProcessed);
    }

    /**
     * 注册 ChatClients 工厂 Bean
     * <p>
     * 集成 Spring AI 原生配置组件，确保创建的 ChatClient 支持观察和监控功能。
     */
    @Bean
    @ConditionalOnMissingBean
    public ChatClients chatClients(ChatModelRegistry registry,
                                   ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                   ChatClientRequestBuilder chatClientRequestBuilder,
                                   ObjectProvider<ObservationRegistry> observationRegistry,
                                   ObjectProvider<ChatClientObservationConvention> chatClientObservationConvention,
                                   ObjectProvider<AdvisorObservationConvention> advisorObservationConvention) {
        return new ChatClients(registry,
                chatClientBuilderConfigurer,
                chatClientRequestBuilder,
                observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP),
                chatClientObservationConvention.getIfUnique(() -> null),
                advisorObservationConvention.getIfUnique(() -> null));
    }

    /**
     * 注册默认的 ChatClient Bean（兼容原有单模型使用方式）
     * <p>
     * 通过 ChatClients 工厂创建，确保集成了观察和监控配置。
     */
    @Bean
    @ConditionalOnMissingBean
    public ChatClient chatClient(ChatClients chatClients) {
        return chatClients.client();
    }

}
