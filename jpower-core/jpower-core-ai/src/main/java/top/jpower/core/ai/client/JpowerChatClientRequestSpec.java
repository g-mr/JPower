package top.jpower.core.ai.client;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.client.advisor.ChatModelCallAdvisor;
import org.springframework.ai.chat.client.advisor.ChatModelStreamAdvisor;
import org.springframework.ai.chat.client.advisor.DefaultAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisorChain;
import org.springframework.ai.chat.client.advisor.observation.AdvisorObservationConvention;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.client.observation.DefaultChatClientObservationConvention;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * chatclient request spec
 *
 * @author mr.g
 */
public class JpowerChatClientRequestSpec extends DefaultChatClient.DefaultChatClientRequestSpec {

    private static final ChatClientObservationConvention DEFAULT_CHAT_CLIENT_OBSERVATION_CONVENTION = new DefaultChatClientObservationConvention();

    private final ObservationRegistry observationRegistry;

    private final ChatClientObservationConvention chatClientObservationConvention;

    @org.springframework.lang.Nullable
    private final AdvisorObservationConvention advisorObservationConvention;

    private final ChatModel chatModel;

    private final ChatClientRequestBuilder chatClientRequestBuilder;

    private final List<Advisor> advisors = new ArrayList<>();

    public JpowerChatClientRequestSpec(ChatModel chatModel, @org.jspecify.annotations.Nullable String userText, Map<String, Object> userParams, Map<String, Object> userMetadata, @org.jspecify.annotations.Nullable String systemText, Map<String, Object> systemParams, Map<String, Object> systemMetadata, List<ToolCallback> toolCallbacks, List<ToolCallbackProvider> toolCallbackProviders, List<Message> messages, List<String> toolNames, List<Media> media, @org.jspecify.annotations.Nullable ChatOptions chatOptions, List<Advisor> advisors, Map<String, Object> advisorParams, ObservationRegistry observationRegistry, @org.jspecify.annotations.Nullable ChatClientObservationConvention chatClientObservationConvention, Map<String, Object> toolContext, @org.jspecify.annotations.Nullable TemplateRenderer templateRenderer, @org.jspecify.annotations.Nullable AdvisorObservationConvention advisorObservationConvention, ChatClientRequestBuilder chatClientRequestBuilder) {
        super(chatModel, userText, userParams, userMetadata, systemText, systemParams, systemMetadata, toolCallbacks, toolCallbackProviders, messages, toolNames, media, chatOptions, advisors, advisorParams, observationRegistry, chatClientObservationConvention, toolContext, templateRenderer, advisorObservationConvention);
        this.observationRegistry = observationRegistry;
        this.advisorObservationConvention = advisorObservationConvention;
        this.advisors.addAll(advisors);
        this.chatModel = chatModel;
        this.chatClientObservationConvention = chatClientObservationConvention != null ? chatClientObservationConvention : DEFAULT_CHAT_CLIENT_OBSERVATION_CONVENTION;
        this.chatClientRequestBuilder = chatClientRequestBuilder;
    }

    @Override
    public ChatClient.CallResponseSpec call() {
        BaseAdvisorChain advisorChain = buildAdvisorChain();
        return new DefaultChatClient.DefaultCallResponseSpec(chatClientRequestBuilder.builder(this), advisorChain,
                this.observationRegistry, this.chatClientObservationConvention);
    }

    @Override
    public ChatClient.StreamResponseSpec stream() {
        BaseAdvisorChain advisorChain = buildAdvisorChain();
        return new DefaultChatClient.DefaultStreamResponseSpec(chatClientRequestBuilder.builder(this), advisorChain,
                this.observationRegistry, this.chatClientObservationConvention);
    }

    private BaseAdvisorChain buildAdvisorChain() {
        // At the stack bottom add the model call advisors.
        // They play the role of the last advisors in the advisor chain.
        this.advisors.add(ChatModelCallAdvisor.builder().chatModel(this.chatModel).build());
        this.advisors.add(ChatModelStreamAdvisor.builder().chatModel(this.chatModel).build());

        return DefaultAroundAdvisorChain.builder(this.observationRegistry)
                .observationConvention(this.advisorObservationConvention)
                .pushAll(this.advisors)
                .build();
    }

}
