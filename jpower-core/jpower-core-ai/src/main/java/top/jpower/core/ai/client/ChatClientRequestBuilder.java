package top.jpower.core.ai.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.DefaultToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.jpower.core.ai.prompt.PromptProcessed;

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
                processedSystemText = promptProcessed.process(processedSystemText);
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
                processedUserText = promptProcessed.process(processedUserText);
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

        // If we have tool-related configuration but no tool or non-tool options,
        // create ToolCallingChatOptions
        if (!inputRequest.getToolNames().isEmpty() || !inputRequest.getToolCallbacks().isEmpty()
                || !inputRequest.getToolCallbackProviders().isEmpty()
                || !CollectionUtils.isEmpty(inputRequest.getToolContext())) {

            if (processedChatOptions == null) {
                processedChatOptions = new DefaultToolCallingChatOptions();
            }
            else if (processedChatOptions instanceof DefaultChatOptions defaultChatOptions) {
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

}
