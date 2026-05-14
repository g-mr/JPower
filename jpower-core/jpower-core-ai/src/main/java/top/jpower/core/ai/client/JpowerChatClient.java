package top.jpower.core.ai.client;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.client.advisor.observation.AdvisorObservationConvention;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 自定义 ChatClient 实现
 * <p>
 * 继承 DefaultChatClient，覆写 prompt() 方法以使用 {@link JpowerChatClientRequestSpec}
 * 替代默认的 DefaultChatClientRequestSpec，实现自定义的请求处理逻辑。
 *
 * @author mr.g
 */
public class JpowerChatClient extends DefaultChatClient {

    private final ChatModel chatModel;
    private final ObservationRegistry observationRegistry;
    @Nullable
    private final ChatClientObservationConvention chatClientObservationConvention;
    @Nullable
    private final AdvisorObservationConvention advisorObservationConvention;
    private final ChatClientRequestBuilder chatClientRequestBuilder;

    public JpowerChatClient(DefaultChatClientRequestSpec defaultChatClientRequest,
                            ChatModel chatModel,
                            ChatClientRequestBuilder chatClientRequestBuilder,
                            ObservationRegistry observationRegistry,
                            @Nullable ChatClientObservationConvention chatClientObservationConvention,
                            @Nullable AdvisorObservationConvention advisorObservationConvention) {
        super(defaultChatClientRequest);
        this.chatModel = chatModel;
        this.observationRegistry = observationRegistry;
        this.chatClientObservationConvention = chatClientObservationConvention;
        this.advisorObservationConvention = advisorObservationConvention;
        this.chatClientRequestBuilder = chatClientRequestBuilder;
    }

    @Override
    public ChatClient.ChatClientRequestSpec prompt() {
        // 通过 super.prompt() 获取带有 Builder 配置默认值的 DefaultChatClientRequestSpec 副本
        DefaultChatClientRequestSpec defaultSpec = (DefaultChatClientRequestSpec) super.prompt();
        return toJpowerSpec(defaultSpec);
    }

    @Override
    public ChatClient.ChatClientRequestSpec prompt(String content) {
        Assert.hasText(content, "content cannot be null or empty");
        return prompt(new Prompt(content));
    }

    @Override
    public ChatClient.ChatClientRequestSpec prompt(Prompt prompt) {
        Assert.notNull(prompt, "prompt cannot be null");
        // 通过 super.prompt(prompt) 获取已应用 Prompt 内容的 DefaultChatClientRequestSpec
        DefaultChatClientRequestSpec defaultSpec = (DefaultChatClientRequestSpec) super.prompt(prompt);
        return toJpowerSpec(defaultSpec);
    }

    /**
     * 将 DefaultChatClientRequestSpec 转换为 JpowerChatClientRequestSpec
     */
    private JpowerChatClientRequestSpec toJpowerSpec(DefaultChatClientRequestSpec spec) {
        return new JpowerChatClientRequestSpec(
                chatModel,
                spec.getUserText(),
                spec.getUserParams(),
                spec.getUserMetadata(),
                spec.getSystemText(),
                spec.getSystemParams(),
                spec.getSystemMetadata(),
                spec.getToolCallbacks(),
                spec.getToolCallbackProviders(),
                spec.getMessages(),
                spec.getToolNames(),
                spec.getMedia(),
                spec.getChatOptions(),
                spec.getAdvisors(),
                spec.getAdvisorParams(),
                observationRegistry,
                chatClientObservationConvention,
                spec.getToolContext(),
                spec.getTemplateRenderer(),
                advisorObservationConvention,
                chatClientRequestBuilder
        );
    }
}
