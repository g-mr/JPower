package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.text.StrPool;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import top.jpower.core.ai.client.ChatClients;
import top.jpower.core.ai.client.JpowerChatOptions;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


/**
 * 信息提取
 */
@Slf4j
@Component(ExtractGranter.GRANT_TYPE)
@RequiredArgsConstructor
public class ExtractGranter implements NodeGranter<UserIntent.Node.ExtractNode> {

    public static final String GRANT_TYPE = "extract";

    /**
     * 系统提示词
     */
    private static final String SYSTEM_PROMPT = "EXTRACT_SYSTEM_PROMPT";

    /**
     * 提示词模板
     */
    private static final String PROMPT_TEMPLATE = "EXTRACT_USER_PROMPT";

    private final ThreadPoolTaskExecutor taskExecutor;

    private final ChatClients chatClients;
    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ExtractNode node) {

        String text = parser.parseExpression(node.getText()).getValue(nodeContext.getContext(), String.class);

        // 如果原始文本是空的则直接返回空
        if (Fc.isBlank(text)) {
            return NodeResult.builder()
                    .nextId(node.getNextNode())
                    .result(new JSONObject())
                    .build();
        }

        CompletableFuture<JSONObject> future = CompletableFuture.supplyAsync(()-> {
            String varInfo = chatClients.client()
                    .prompt()
                    .options(JpowerChatOptions.builder()
                            .temperature(0.5)
                            .maxTokens(1000)
                            .jsonMode()
                            .build())
                    .system(SYSTEM_PROMPT)
                    .user(u -> u.text(PROMPT_TEMPLATE)
                            .param("text", text)
                            .param("keys", node.getKeys().entrySet().stream()
                                    .map(entry -> " - " + entry.getKey() + "：" + entry.getValue())
                                    .collect(Collectors.joining(StrPool.LF)))
                    )
                    .call()
                    .content();

            log.info("大模型返回得信息====>>{}", varInfo);
            if (JSONUtil.isTypeJSONObject(varInfo)){
                return JSON.parseObject(varInfo);
            } else {
                log.error("信息提取失败[原始文本={}], 提取结果==>>{}；提取信息=>{}", text, varInfo, node.getKeys());
                return new JSONObject();
            }
        }, taskExecutor);

        try {
            return NodeResult.builder()
                    .nextId(node.getNextNode())
                    .result(node.getAsync()?future:future.get(1, TimeUnit.MINUTES))
                    .build();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("信息提取失败[原始文本={}], 异常==>>{}", text, e.getMessage());
            return NodeResult.builder()
                    .nextId(node.getNextNode())
                    .result(new JSONObject())
                    .build();
        }
    }

}
