package top.jpower.core.ai.utils;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import top.jpower.core.util.utils.Fc;

import java.util.concurrent.*;

/**
 * AI 工具调用实现
 * <p>
 * 支持多模型调用，通过 ChatClients 工厂获取不同模型的 ChatClient。
 *
 * @author mr.g
 */
@Slf4j
@UtilityClass
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class AIUtil extends cn.hutool.ai.AIUtil {

    /**
     * 执行流式调用
     *
     * @param client ChatClient
     * @param userPromptKey   用户提示语
     * @return Flux<String>
     */
    public Flux<String> doStream(ChatClient client, String userPromptKey) {
        return doStream(client, null, userPromptKey);
    }

    /**
     * 执行流式调用
     *
     * @param client ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @return Flux<String>
     */
    public Flux<String> doStream(ChatClient client, String systemPromptKey, String userPromptKey) {
        if (Fc.isBlank(userPromptKey)) {
            return Flux.empty();
        }

        ChatClient.ChatClientRequestSpec requestSpec = client.prompt();
        if (Fc.isNotBlank(systemPromptKey)) {
            requestSpec.system(systemPromptKey);
        }

        return requestSpec
                .user(userPromptKey)
                .stream()
                .content();
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String userPromptKey,
                                             Long timeout,
                                             TimeUnit unit) {
        return doAsync(client, null, userPromptKey, timeout, unit);
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String systemPromptKey,
                                             String userPromptKey,
                                             Long timeout,
                                             TimeUnit unit) {
        CompletableFuture<String> future = doAsync(client, systemPromptKey, userPromptKey);
        if (timeout > 0) {
            return future.orTimeout(timeout, unit);
        } else {
            return future;
        }
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @param defaultContent  默认内容
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String userPromptKey,
                                             Long timeout,
                                             TimeUnit unit,
                                             String defaultContent) {
        return doAsync(client, null, userPromptKey, timeout, unit, defaultContent);
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @param defaultContent  默认内容
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String systemPromptKey,
                                             String userPromptKey,
                                             Long timeout,
                                             TimeUnit unit,
                                             String defaultContent) {
        CompletableFuture<String> future = doAsync(client, systemPromptKey, userPromptKey);
        if (timeout > 0) {
            return future.completeOnTimeout(defaultContent, timeout, unit);
        } else {
            return future;
        }
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String userPromptKey) {
        return doAsync(client, null, userPromptKey);
    }

    /**
     * 执行异步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @return CompletableFuture<String>
     */
    public CompletableFuture<String> doAsync(ChatClient client,
                                             String systemPromptKey,
                                             String userPromptKey) {
        if (Fc.isBlank(userPromptKey)) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            ChatClient.ChatClientRequestSpec requestSpec = client.prompt();
            if (Fc.isNotBlank(systemPromptKey)) {
                requestSpec.system(systemPromptKey);
            }

            return requestSpec
                    .user(userPromptKey)
                    .call()
                    .content();
        });
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @param defaultContent  默认内容
     * @return 结果
     */
    public String doSync(ChatClient client,
                         String userPromptKey,
                         Long timeout,
                         TimeUnit unit,
                         String defaultContent) {
        return doSync(client, null, userPromptKey, timeout, unit, defaultContent);
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @param defaultContent  默认内容
     * @return 结果
     */
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public String doSync(ChatClient client,
                         String systemPromptKey,
                         String userPromptKey,
                         Long timeout,
                         TimeUnit unit,
                         String defaultContent) {
        Future<String> future = ThreadUtil.execAsync(() ->
                doSync(client, systemPromptKey, userPromptKey)
        );
        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            log.warn("AI调用超时[{}],超时时长：{}", userPromptKey, unit.toSeconds(timeout));
            return defaultContent;
        }
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @return 结果
     */
    public String doSync(ChatClient client,
                         String userPromptKey,
                         Long timeout,
                         TimeUnit unit) throws TimeoutException {
        return doSync(client, null, userPromptKey, timeout, unit);
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @param timeout         超时时间
     * @param unit            时间单位
     * @return 结果
     */
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public String doSync(ChatClient client,
                         String systemPromptKey,
                         String userPromptKey,
                         Long timeout,
                         TimeUnit unit) throws TimeoutException {
        Future<String> future = ThreadUtil.execAsync(() ->
                doSync(client, systemPromptKey, userPromptKey)
        );
        return future.get(timeout, unit);
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param userPromptKey   用户提示语
     * @return 结果
     */
    public String doSync(ChatClient client,
                         String userPromptKey) {
        return doSync(client, null, userPromptKey);
    }

    /**
     * 执行同步调用
     *
     * @param client          ChatClient
     * @param systemPromptKey 系统提示语
     * @param userPromptKey   用户提示语
     * @return 结果
     */
    public String doSync(ChatClient client,
                         String systemPromptKey,
                         String userPromptKey) {
        if (Fc.isBlank(userPromptKey)) {
            return null;
        }

        ChatClient.ChatClientRequestSpec requestSpec = client.prompt();
        if (Fc.isNotBlank(systemPromptKey)) {
            requestSpec.system(systemPromptKey);
        }
        return requestSpec
                .user(userPromptKey)
                .call()
                .content();
    }
}



