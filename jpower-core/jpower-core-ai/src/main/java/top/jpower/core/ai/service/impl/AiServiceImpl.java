package top.jpower.core.ai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import top.jpower.core.ai.service.AiService;
import top.jpower.core.util.utils.Fc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * AI 服务实现
 *
 * @author mr.g
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Override
    public String chat(String prompt, int timeoutSeconds) {
        return chatStream(prompt, timeoutSeconds);
    }

    @Override
    public String chatStream(String prompt, int timeoutSeconds) {
        if (Fc.isBlank(prompt)) {
            return null;
        }

        log.info("AI模型调用开始，超时: {}秒", timeoutSeconds);
        long startTime = System.currentTimeMillis();

        try {
            String result = chatClient.prompt()
                    .user(prompt)
                    .stream()
                    .content()
                    .collectList()
                    .block(java.time.Duration.ofSeconds(timeoutSeconds))
                    .stream()
                    .collect(Collectors.joining());

            long duration = System.currentTimeMillis() - startTime;
            log.info("AI模型调用完成，耗时: {}ms", duration);

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("AI模型调用异常，耗时: {}ms，错误: {}", duration, e.getMessage());
            throw new RuntimeException("AI调用异常: " + e.getMessage(), e);
        }
    }

    @Override
    public CompletableFuture<String> chatAsync(String prompt) {
        return chatAsync(prompt, 60);
    }

    @Override
    public CompletableFuture<String> chatAsync(String prompt, int timeoutSeconds) {
        if (Fc.isBlank(prompt)) {
            return CompletableFuture.completedFuture(null);
        }

        log.debug("AI模型异步调用开始，超时: {}秒", timeoutSeconds);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return chatClient.prompt()
                        .user(prompt)
                        .stream()
                        .content()
                        .collectList()
                        .block(java.time.Duration.ofSeconds(timeoutSeconds))
                        .stream()
                        .collect(Collectors.joining());
            } catch (Exception e) {
                log.error("AI模型异步调用异常: {}", e.getMessage());
                return null;
            }
        });
    }

    /**
     * 带超时控制的模型调用（返回异常）
     */
    public String chatWithTimeout(String prompt, int timeoutSeconds) throws TimeoutException {
        if (Fc.isBlank(prompt)) {
            return null;
        }

        log.debug("AI模型调用开始（带超时控制），超时: {}秒", timeoutSeconds);
        long startTime = System.currentTimeMillis();

        try {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return chatClient.prompt()
                        .user(prompt)
                        .stream()
                        .content()
                        .collectList()
                        .block()
                        .stream()
                        .collect(Collectors.joining());
            });

            String result = future.get(timeoutSeconds, TimeUnit.SECONDS);

            long duration = System.currentTimeMillis() - startTime;
            log.debug("AI模型调用完成，耗时: {}ms", duration);

            return result;
        } catch (TimeoutException e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("AI模型调用超时，耗时: {}ms，超时时间: {}秒", duration, timeoutSeconds);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("AI模型调用异常，耗时: {}ms，错误: {}", duration, e.getMessage());
            throw new RuntimeException("AI调用异常: " + e.getMessage(), e);
        }
    }

}
