package top.jpower.core.ai.client;

import io.micrometer.observation.ObservationRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.client.advisor.observation.AdvisorObservationConvention;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import top.jpower.core.ai.enums.ChatModelType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChatClient 工厂类
 * <p>
 * 提供便捷的方式获取不同模型对应的 ChatClient 实例。
 * 集成 Spring AI 原生的 ObservationRegistry、ChatClientObservationConvention、
 * AdvisorObservationConvention 和 ChatClientBuilderConfigurer 配置组件。
 * <p>
 * 使用示例:
 * <pre>
 * &#64;Autowired
 * private ChatClients chatClients;
 *
 * // 获取默认 ChatClient
 * ChatClient client = chatClients.client();
 *
 * // 获取指定模型的 ChatClient
 * ChatClient deepseek = chatClients.client(ChatModelType.DEEPSEEK);
 * ChatClient qwen = chatClients.client(ChatModelType.DASHSCOPE);
 *
 * // 获取指定模型的 ChatClient.Builder（用于自定义配置）
 * ChatClient custom = chatClients.builder(ChatModelType.DEEPSEEK)
 *     .defaultSystem("你是一个助手")
 *     .build();
 * </pre>
 *
 * @author mr.g
 */
@Slf4j
public class ChatClients {

    @Getter
    private final ChatModelRegistry registry;
    private final ChatClientBuilderConfigurer builderConfigurer;
    private final ObservationRegistry observationRegistry;
    private final ChatClientObservationConvention chatClientObservationConvention;
    private final AdvisorObservationConvention advisorObservationConvention;
    private final ChatClientRequestBuilder chatClientRequestBuilder;

    private final Map<ChatModelType, ChatClient> clientCache = new ConcurrentHashMap<>();
    private volatile ChatClient defaultClient;

    public ChatClients(ChatModelRegistry registry,
                       ChatClientBuilderConfigurer builderConfigurer,
                       ChatClientRequestBuilder chatClientRequestBuilder,
                       ObservationRegistry observationRegistry,
                       ChatClientObservationConvention chatClientObservationConvention,
                       AdvisorObservationConvention advisorObservationConvention) {
        this.registry = registry;
        this.builderConfigurer = builderConfigurer;
        this.observationRegistry = observationRegistry;
        this.chatClientObservationConvention = chatClientObservationConvention;
        this.advisorObservationConvention = advisorObservationConvention;
        this.chatClientRequestBuilder = chatClientRequestBuilder;
    }

    /**
     * 获取默认 ChatClient（使用主模型）
     *
     * @return 默认 ChatClient
     * @throws IllegalStateException 如果没有可用模型
     */
    public ChatClient client() {
        if (defaultClient == null) {
            synchronized (this) {
                if (defaultClient == null) {
                    ChatModel primary = registry.getPrimary();
                    if (primary == null) {
                        throw new IllegalStateException("没有可用的 ChatModel，请检查 AI 模型配置");
                    }
                    defaultClient = createClient(primary);
                }
            }
        }
        return defaultClient;
    }

    /**
     * 获取指定模型类型的 ChatClient
     *
     * @param type 模型类型
     * @return ChatClient 实例
     * @throws IllegalArgumentException 如果指定模型不存在
     */
    public ChatClient client(ChatModelType type) {
        return clientCache.computeIfAbsent(type, t -> {
            ChatModel model = getModelOrThrow(t);
            return createClient(model);
        });
    }

    /**
     * 获取指定模型类型的 ChatClient.Builder（用于自定义配置）
     * <p>
     * 已集成 ObservationRegistry 和 ChatClientBuilderConfigurer 配置，
     * 用户可在此基础上继续自定义 Builder。
     *
     * @param type 模型类型
     * @return ChatClient.Builder
     * @throws IllegalArgumentException 如果指定模型不存在
     */
    public ChatClient.Builder builder(ChatModelType type) {
        ChatModel model = getModelOrThrow(type);
        return createBuilder(model);
    }

    /**
     * 获取默认模型的 ChatClient.Builder
     *
     * @return ChatClient.Builder
     */
    public ChatClient.Builder builder() {
        ChatModel primary = registry.getPrimary();
        if (primary == null) {
            throw new IllegalStateException("没有可用的 ChatModel，请检查 AI 模型配置");
        }
        return createBuilder(primary);
    }

    /**
     * 获取默认主模型的类型
     * <p>
     * 用于在运行时判断当前使用的模型类型，以便动态选择匹配的 ChatOptions。
     *
     * @return 主模型的 ChatModelType，如果无法推断则返回 null
     */
    public ChatModelType primaryType() {
        return registry.getPrimaryType();
    }

    /**
     * 获取可移植的 ChatOptions 构建器
     * <p>
     * 返回的 Builder 构建出的 {@link JpowerChatOptions} 会在请求管道中
     * 自动转换为与当前 ChatModel 匹配的特定 ChatOptions 类型。
     * <p>
     * 使用示例:
     * <pre>
     * chatClients.client()
     *     .prompt()
     *     .options(chatClients.optionsBuilder()
     *         .temperature(0.1)
     *         .maxTokens(600)
     *         .jsonMode()
     *         .build())
     *     .user("分析这段文本")
     *     .call().content();
     * </pre>
     *
     * @return JpowerChatOptions.Builder
     */
    public JpowerChatOptions.Builder optionsBuilder() {
        return JpowerChatOptions.builder();
    }

    /**
     * 判断指定模型类型是否可用
     */
    public boolean isAvailable(ChatModelType type) {
        return registry.contains(type);
    }

    /**
     * 创建集成了 Observation 和 Configurer 的 ChatClient.Builder
     */
    private ChatClient.Builder createBuilder(ChatModel model) {
        ChatClient.Builder builder = ChatClient.builder(model,
                observationRegistry,
                chatClientObservationConvention,
                advisorObservationConvention);
        return builderConfigurer.configure(builder);
    }

    /**
     * 创建使用 JpowerChatClientRequestSpec 的自定义 ChatClient
     * <p>
     * 先通过标准 Builder 构建出带有默认配置的 DefaultChatClientRequestSpec，
     * 再包装为 JpowerChatClient 以使用自定义的请求处理逻辑。
     */
    private ChatClient createClient(ChatModel model) {
        // 通过标准 builder 构建，获取配置好的 DefaultChatClient
        ChatClient.Builder builder = createBuilder(model);
        DefaultChatClient defaultClient = (DefaultChatClient) builder.build();
        // 获取 builder 配置好的默认 RequestSpec（包含 defaultSystem、defaultAdvisors 等）
        DefaultChatClient.DefaultChatClientRequestSpec defaultSpec =
                (DefaultChatClient.DefaultChatClientRequestSpec) defaultClient.prompt();
        // 使用 JpowerChatClient 包装，prompt() 将返回 JpowerChatClientRequestSpec
        return new JpowerChatClient(defaultSpec,
                model,
                chatClientRequestBuilder,
                observationRegistry,
                chatClientObservationConvention,
                advisorObservationConvention);
    }

    /**
     * 获取模型，不存在时抛出异常
     */
    private ChatModel getModelOrThrow(ChatModelType type) {
        ChatModel model = registry.getModel(type);
        if (model == null) {
            throw new IllegalArgumentException("未找到模型类型 [" + type.getKey() + "] 对应的 ChatModel，" +
                    "请确认已添加相应依赖并配置正确。可用模型: " + registry.getRegisteredTypes());
        }
        return model;
    }
}
