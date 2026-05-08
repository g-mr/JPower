package top.jpower.core.ai.service;

import java.util.concurrent.CompletableFuture;

/**
 * AI 服务接口
 *
 * @author mr.g
 */
public interface AiService {

    /**
     * 调用 AI 模型（带超时）
     *
     * @param prompt         提示词
     * @param timeoutSeconds 超时时间（秒）
     * @return AI 返回结果
     */
    String chat(String prompt, int timeoutSeconds);

    /**
     * 调用 AI 模型（流式，带超时）
     *
     * @param prompt         提示词
     * @param timeoutSeconds 超时时间（秒）
     * @return AI 返回结果
     */
    String chatStream(String prompt, int timeoutSeconds);

    /**
     * 调用 AI 模型（异步执行）
     *
     * @param prompt   提示词
     * @return 异步结果
     */
    CompletableFuture<String> chatAsync(String prompt);

    /**
     * 调用 AI 模型（异步执行，带超时）
     *
     * @param prompt         提示词
     * @param timeoutSeconds 超时时间（秒）
     * @return 异步结果
     */
    CompletableFuture<String> chatAsync(String prompt, int timeoutSeconds);

}
