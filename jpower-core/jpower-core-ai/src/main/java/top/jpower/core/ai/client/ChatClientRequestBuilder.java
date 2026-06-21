package top.jpower.core.ai.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.DefaultToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.jpower.core.ai.enums.ChatModelType;
import top.jpower.core.ai.prompt.PromptProcessed;
import top.jpower.core.util.utils.Fc;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for converting {@link JpowerChatClientRequestSpec} to {@link ChatClientRequest}.
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class ChatClientRequestBuilder {

    private final PromptProcessed promptProcessed;

    public ChatClientRequest builder(JpowerChatClientRequestSpec inputRequest) {
        Assert.notNull(inputRequest, "inputRequest cannot be null");

        /*
         * ==========* MESSAGES * ==========
         */

        List<Message> processedMessages = new ArrayList<>();

        // System Text => First in the list
        String processedSystemText = inputRequest.getSystemText();
        if (StringUtils.hasText(processedSystemText)) {
            if (promptProcessed != null){
                String systemText = promptProcessed.process(processedSystemText);
                if (Fc.isNotBlank(systemText)) {
                    processedSystemText = systemText;
                }
            } else {
                log.warn("promptProcessed is null, please check the promptProcessed configuration");
            }
            if (!CollectionUtils.isEmpty(inputRequest.getSystemParams())) {
                processedSystemText = PromptTemplate.builder()
                        .template(processedSystemText)
                        .variables(inputRequest.getSystemParams())
                        .renderer(inputRequest.getTemplateRenderer())
                        .build()
                        .render();
            }
            processedMessages.add(SystemMessage.builder()
                    .text(processedSystemText)
                    .metadata(inputRequest.getSystemMetadata())
                    .build());
        }

        // Messages => In the middle of the list
        if (!CollectionUtils.isEmpty(inputRequest.getMessages())) {
            processedMessages.addAll(inputRequest.getMessages());
        }

        // User Text => Last in the list
        String processedUserText = inputRequest.getUserText();
        if (StringUtils.hasText(processedUserText)) {
            if (promptProcessed != null){
                String userText = promptProcessed.process(processedUserText);
                if (Fc.isNotBlank(userText)) {
                    processedUserText = userText;
                }
            } else {
                log.warn("promptProcessed is null, please check the promptProcessed configuration");
            }
            if (!CollectionUtils.isEmpty(inputRequest.getUserParams())) {
                processedUserText = PromptTemplate.builder()
                        .template(processedUserText)
                        .variables(inputRequest.getUserParams())
                        .renderer(inputRequest.getTemplateRenderer())
                        .build()
                        .render();
            }
            processedMessages.add(UserMessage.builder()
                    .text(processedUserText)
                    .media(inputRequest.getMedia())
                    .metadata(inputRequest.getUserMetadata())
                    .build());
        }

        /*
         * ==========* OPTIONS * ==========
         */

        ChatOptions processedChatOptions = inputRequest.getChatOptions();

        // Convert JpowerChatOptions to model-specific options
        if (processedChatOptions instanceof JpowerChatOptions jpowerOpts) {
            processedChatOptions = convertJpowerOptions(jpowerOpts, inputRequest.getChatModel());
        }

        // If we have tool-related configuration but no tool or non-tool options,
        // create ToolCallingChatOptions
        if (!inputRequest.getToolNames().isEmpty() || !inputRequest.getToolCallbacks().isEmpty()
                || !inputRequest.getToolCallbackProviders().isEmpty()
                || !CollectionUtils.isEmpty(inputRequest.getToolContext())) {

            if (processedChatOptions == null) {
                processedChatOptions = new DefaultToolCallingChatOptions();
            } else if (processedChatOptions instanceof DefaultChatOptions defaultChatOptions
                    && !(processedChatOptions instanceof ToolCallingChatOptions)) {
                processedChatOptions = ModelOptionsUtils.copyToTarget(defaultChatOptions, ChatOptions.class,
                        DefaultToolCallingChatOptions.class);
            }
        }

        if (processedChatOptions instanceof ToolCallingChatOptions toolCallingChatOptions) {
            if (!inputRequest.getToolNames().isEmpty()) {
                Set<String> toolNames = ToolCallingChatOptions
                        .mergeToolNames(new HashSet<>(inputRequest.getToolNames()), toolCallingChatOptions.getToolNames());
                toolCallingChatOptions.setToolNames(toolNames);
            }

            // Lazily resolve ToolCallbackProvider instances to ToolCallback instances
            List<ToolCallback> allToolCallbacks = new ArrayList<>(inputRequest.getToolCallbacks());
            for (var provider : inputRequest.getToolCallbackProviders()) {
                allToolCallbacks.addAll(java.util.List.of(provider.getToolCallbacks()));
            }

            if (!allToolCallbacks.isEmpty()) {
                List<ToolCallback> toolCallbacks = ToolCallingChatOptions.mergeToolCallbacks(allToolCallbacks,
                        toolCallingChatOptions.getToolCallbacks());
                ToolCallingChatOptions.validateToolCallbacks(toolCallbacks);
                toolCallingChatOptions.setToolCallbacks(toolCallbacks);
            }
            if (!CollectionUtils.isEmpty(inputRequest.getToolContext())) {
                Map<String, Object> toolContext = ToolCallingChatOptions.mergeToolContext(inputRequest.getToolContext(),
                        toolCallingChatOptions.getToolContext());
                toolCallingChatOptions.setToolContext(toolContext);
            }
        }

        /*
         * ==========* REQUEST * ==========
         */

        return ChatClientRequest.builder()
                .prompt(Prompt.builder().messages(processedMessages).chatOptions(processedChatOptions).build())
                .context(new ConcurrentHashMap<>(inputRequest.getAdvisorParams()))
                .build();
    }

//    /**
//     * 将 JpowerChatOptions 转换为与当前 ChatModel 匹配的模型特定 ChatOptions
//     * <p>
//     * 转换策略：
//     * <ul>
//     *   <li>通用字段（temperature, maxTokens 等）通过 ModelOptionsUtils.copyToTarget 映射到目标类型</li>
//     *   <li>responseType 通过反射设置模型对应的 responseFormat 对象</li>
//     *   <li>转换失败时优雅降级为通用 DefaultChatOptions</li>
//     * </ul>
//     */
//    @SuppressWarnings("unchecked")
//    private ChatOptions convertJpowerOptions(JpowerChatOptions jpowerOpts, ChatModel chatModel) {
//        ChatOptions modelDefaults = chatModel.getDefaultOptions();
//
//        // No model defaults available, use common options
//        if (modelDefaults == null) {
//            return toDefaultToolCallingOptions(jpowerOpts);
//        }
//
//        Class<? extends ChatOptions> targetClass = (Class<? extends ChatOptions>) modelDefaults.getClass();
//
//        try {
//            // Copy common ChatOptions fields to model-specific type
//            ChatOptions converted = ModelOptionsUtils.copyToTarget(jpowerOpts, ChatOptions.class, targetClass);
//
//            // Set responseFormat if requested
//            if (jpowerOpts.getResponseType() == JpowerChatOptions.ResponseType.JSON) {
//                setResponseFormat(converted, chatModel);
//            }
//
//            return converted;
//        } catch (Exception e) {
//            log.warn("JpowerChatOptions 转换为 {} 失败，降级为通用选项: {}",
//                    targetClass.getSimpleName(), e.getMessage());
//            return toDefaultToolCallingOptions(jpowerOpts);
//        }
//    }

    /**
     * 将 JpowerChatOptions 转换为与当前 ChatModel 匹配的模型特定 ChatOptions
     * <p>
     * 转换策略（解决 DashScope builder 默认值覆盖配置文件的问题）：
     * <ol>
     *   <li>以模型默认配置（来自 application.yml）为基底，调用 {@code copy()} 创建副本</li>
     *   <li>仅覆盖用户在 JpowerChatOptions 中显式设置（非 null）的通用字段</li>
     *   <li>仅覆盖用户显式设置的模型特有能力字段（enableSearch、enableThinking 等）</li>
     *   <li>处理可移植的 responseType 到模型特定 responseFormat 的转换</li>
     * </ol>
     * <p>
     * 这样确保了：未显式设置的参数（如 DashScope 的 multiModel、enableSearch、
     * incrementalOutput、enableThinking）始终继承配置文件中的值，不会被 builder 默认值覆盖。
     */
    private ChatOptions convertJpowerOptions(JpowerChatOptions jpowerOpts, ChatModel chatModel) {
        ChatOptions modelDefaults = chatModel.getDefaultOptions();

        // No model defaults available, use common fallback options
        if (modelDefaults == null) {
            return toDefaultToolCallingOptions(jpowerOpts);
        }

        try {
            // 1. 以模型默认配置为基底（保留所有配置文件中的值，含 DashScope 特有参数）
            ChatOptions result = modelDefaults.copy();

            // 2. 仅覆盖用户显式设置的通用字段
            applyCommonOverrides(result, jpowerOpts);

            // 3. 覆盖用户显式设置的模型特有能力字段
            applyCapabilityOverrides(result, jpowerOpts);

            // 4. 处理可移植的 responseType
            if (jpowerOpts.getResponseType() == JpowerChatOptions.ResponseType.JSON) {
                setResponseFormat(result, chatModel);
            }

            return result;
        } catch (Exception e) {
            log.warn("JpowerChatOptions 转换为 {} 失败，降级为通用选项: {}",
                    modelDefaults.getClass().getSimpleName(), e.getMessage());
            return toDefaultToolCallingOptions(jpowerOpts);
        }
    }

    /**
     * 覆盖模型选项中的通用字段（仅覆盖 JpowerChatOptions 中非 null 的字段）
     * <p>
     * 通用字段名在不同模型中可能有差异（如 maxTokens vs maxToken），
     * 此方法会尝试多种 setter 名称以兼容各模型实现。
     */
    private void applyCommonOverrides(ChatOptions target, JpowerChatOptions source) {
        if (source.getModel() != null) {
            invokeSetter(target, "setModel", source.getModel(), String.class);
        }
        if (source.getTemperature() != null) {
            invokeSetter(target, "setTemperature", source.getTemperature(), Double.class);
        }
        if (source.getMaxTokens() != null) {
            // 兼容不同模型的字段名差异：maxTokens vs maxToken
            if (!invokeSetter(target, "setMaxTokens", source.getMaxTokens(), Integer.class)) {
                invokeSetter(target, "setMaxToken", source.getMaxTokens(), Integer.class);
            }
        }
        if (source.getTopP() != null) {
            invokeSetter(target, "setTopP", source.getTopP(), Double.class);
        }
        if (source.getTopK() != null) {
            invokeSetter(target, "setTopK", source.getTopK(), Integer.class);
        }
        if (source.getFrequencyPenalty() != null) {
            invokeSetter(target, "setFrequencyPenalty", source.getFrequencyPenalty(), Double.class);
        }
        if (source.getPresencePenalty() != null) {
            invokeSetter(target, "setPresencePenalty", source.getPresencePenalty(), Double.class);
        }
        if (source.getStopSequences() != null) {
            invokeSetter(target, "setStopSequences", source.getStopSequences(), List.class);
            // 部分模型使用 setStop
            invokeSetter(target, "setStop", source.getStopSequences(), List.class);
        }
    }

    /**
     * 覆盖模型特有的能力字段（仅覆盖 JpowerChatOptions 中非 null 的字段）
     * <p>
     * 未设置（null）的能力字段将保持模型默认配置中的值，不会被覆盖。
     * 这解决了 DashScope builder 会将 enableSearch 等字段设为 false 的问题。
     */
    private void applyCapabilityOverrides(ChatOptions target, JpowerChatOptions source) {
        if (source.getEnableSearch() != null) {
            invokeSetter(target, "setEnableSearch", source.getEnableSearch(), Boolean.class);
        }
        if (source.getEnableThinking() != null) {
            invokeSetter(target, "setEnableThinking", source.getEnableThinking(), Boolean.class);
        }
        if (source.getMultiModel() != null) {
            invokeSetter(target, "setMultiModel", source.getMultiModel(), Boolean.class);
        }
        if (source.getIncrementalOutput() != null) {
            invokeSetter(target, "setIncrementalOutput", source.getIncrementalOutput(), Boolean.class);
        }
    }

    /**
     * 通过反射调用 setter 方法
     *
     * @return true 如果成功调用，false 如果方法不存在或调用失败
     */
    private boolean invokeSetter(Object target, String methodName, Object value, Class<?> paramType) {
        try {
            Method method = target.getClass().getMethod(methodName, paramType);
            method.invoke(target, value);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            log.debug("调用 {}.{}({}) 失败: {}", target.getClass().getSimpleName(),
                    methodName, value, e.getMessage());
            return false;
        }
    }

    /**
     * 通过反射在模型特定 Options 上设置 responseFormat
     */
    private void setResponseFormat(ChatOptions options, ChatModel chatModel) {
        ChatModelType type = ChatModelRegistry.inferType(chatModel);
        if (type == null) {
            return;
        }

        try {
            ClassLoader cl = options.getClass().getClassLoader();
            Object responseFormat = createResponseFormat(type, cl);
            if (responseFormat == null) {
                return;
            }

            // Find setResponseFormat method on the options class
            for (var method : options.getClass().getMethods()) {
                if ("setResponseFormat".equals(method.getName()) && method.getParameterCount() == 1) {
                    if (method.getParameterTypes()[0].isInstance(responseFormat)) {
                        method.invoke(options, responseFormat);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            log.debug("设置 responseFormat 失败 [{}]: {}", type.getKey(), e.getMessage());
        }
    }

    /**
     * 根据模型类型动态创建对应的 ResponseFormat 对象
     * <p>
     * DashScope 模型支持两种 JAR 包实现：
     * <ul>
     *   <li>spring-ai-alibaba-dashscope（原版）：ResponseFormat 类位于 {@code com.alibaba.cloud.ai.dashscope.api} 包</li>
     *   <li>spring-ai-alibaba-dashscope-sdk（SDK 版）：无独立 ResponseFormat 类，JSON 模式不可用</li>
     * </ul>
     * 此方法会依次尝试多个类路径，以兼容不同版本的 JAR 包。
     */
    @Nullable
    private Object createResponseFormat(ChatModelType type, ClassLoader cl) {
        try {
            return switch (type) {
                case DASHSCOPE -> createDashScopeResponseFormat(cl);
                case DEEPSEEK -> createViaBuilder(cl,
                        "org.springframework.ai.deepseek.api.DeepSeekApi$ChatCompletionRequest$ResponseFormat",
                        "type", findEnumValue(cl,
                                "org.springframework.ai.deepseek.api.DeepSeekApi$ChatCompletionRequest$ResponseFormat$Type",
                                "JSON_OBJECT"));
                case OPENAI, AZURE_OPENAI -> createViaBuilder(cl,
                        "org.springframework.ai.openai.api.ResponseFormat",
                        "type", findEnumValue(cl,
                                "org.springframework.ai.openai.api.ResponseFormat$Type",
                                "JSON_OBJECT"));
                default -> null;
            };
        } catch (Exception e) {
            log.debug("创建 responseFormat 对象失败 [{}]: {}", type.getKey(), e.getMessage());
            return null;
        }
    }

    /**
     * 创建 DashScope 的 ResponseFormat 对象，兼容原版和 SDK 版 JAR 包
     * <p>
     * 尝试以下类路径（按优先级）：
     * <ol>
     *   <li>{@code com.alibaba.cloud.ai.dashscope.api.DashScopeResponseFormat}（原版 1.1.x）</li>
     *   <li>{@code com.alibaba.cloud.ai.dashscope.chat.DashScopeResponseFormat}（旧版本兼容）</li>
     * </ol>
     * SDK 版（spring-ai-alibaba-dashscope-sdk）没有独立的 ResponseFormat 类，
     * 此方法将返回 null，JSON 模式将不可用。
     *
     * @param cl 类加载器
     * @return ResponseFormat 对象，无法创建时返回 null
     */
    @Nullable
    private Object createDashScopeResponseFormat(ClassLoader cl) {
        // 原版 JAR 中的类路径（api 包）
        Object result = createViaBuilder(cl,
                "com.alibaba.cloud.ai.dashscope.api.DashScopeResponseFormat",
                "type", "json_object");
        if (result != null) {
            return result;
        }

        // 旧版本兼容（chat 包）
        result = createViaBuilder(cl,
                "com.alibaba.cloud.ai.dashscope.chat.DashScopeResponseFormat",
                "type", "json_object");
        if (result != null) {
            return result;
        }

        log.debug("DashScope ResponseFormat 类未找到，可能使用的是 SDK 版 JAR（spring-ai-alibaba-dashscope-sdk），JSON 模式不可用");
        return null;
    }

    /**
     * 通过 builder 模式反射创建对象
     */
    @Nullable
    private Object createViaBuilder(ClassLoader cl, String className, String setterName, Object value) {
        try {
            Class<?> clazz = cl.loadClass(className);

            // Try builder() pattern
            var builderMethod = clazz.getMethod("builder");
            Object builder = builderMethod.invoke(null);

            // Set the field
            for (var method : builder.getClass().getMethods()) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    method.invoke(builder, value);
                    break;
                }
            }

            // Build
            var buildMethod = builder.getClass().getMethod("build");
            return buildMethod.invoke(builder);
        } catch (Exception e) {
            // Try direct constructor
            try {
                Class<?> clazz = cl.loadClass(className);
                var constructors = clazz.getConstructors();
                for (var ctor : constructors) {
                    if (ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0].isInstance(value)) {
                        return ctor.newInstance(value);
                    }
                }
            } catch (Exception ignored) {
            }
            return null;
        }
    }

    /**
     * 查找枚举值
     */
    @Nullable
    private Object findEnumValue(ClassLoader cl, String enumClassName, String valueName) {
        try {
            Class<?> enumClass = cl.loadClass(enumClassName);
            if (enumClass.isEnum()) {
                for (Object constant : enumClass.getEnumConstants()) {
                    if (((Enum<?>) constant).name().equals(valueName)) {
                        return constant;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 将 JpowerChatOptions 转换为 DefaultToolCallingChatOptions（降级方案）
     */
    private DefaultToolCallingChatOptions toDefaultToolCallingOptions(JpowerChatOptions opts) {
        DefaultToolCallingChatOptions result = new DefaultToolCallingChatOptions();
        result.setTemperature(opts.getTemperature());
        result.setMaxTokens(opts.getMaxTokens());
        result.setTopP(opts.getTopP());
        result.setTopK(opts.getTopK());
        result.setModel(opts.getModel());
        result.setFrequencyPenalty(opts.getFrequencyPenalty());
        result.setPresencePenalty(opts.getPresencePenalty());
        result.setStopSequences(opts.getStopSequences());
        return result;
    }

}
