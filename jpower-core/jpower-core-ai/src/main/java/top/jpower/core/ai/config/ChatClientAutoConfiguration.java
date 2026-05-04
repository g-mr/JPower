package top.jpower.core.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * ChatClient 自动配置
 *
 * @author mr.g
 */
@AutoConfiguration
public class ChatClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}