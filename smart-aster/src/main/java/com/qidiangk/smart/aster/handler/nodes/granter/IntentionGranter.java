package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiHangupException;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import top.jpower.core.ai.client.ChatClients;
import top.jpower.core.ai.client.JpowerChatOptions;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.qidiangk.smart.aster.constants.ConstantUtil.MUSIC;

/**
 * 意图判断
 */
@Slf4j
@Component(IntentionGranter.GRANT_TYPE)
@RequiredArgsConstructor
public class IntentionGranter implements NodeGranter<UserIntent.Node.IntentionNode> {

    public static final String GRANT_TYPE = "intention";

    /**
     * 正则缓存
     **/
    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    /**
     * 系统提示词模板
     */
    private static final String SYSTEM_PROMPT = "INTENT_SYSTEM_PROMPT";

    /**
     * 提示词模板
     */
    private static final String PROMPT_TEMPLATE = "INTENT_USER_PROMPT";

    private static final String OTHER_CODE = "other";
    private static final String OTHER_DESCRIPTION = "其他意图都不适配的时候输出这个。";

    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();
    private final ChatClients chatClients;

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.IntentionNode node) {
        TimeInterval interval = DateUtil.timer();
        interval.start();

        // 所有意图
        List<UserIntent.Node.IntentionNode.Intention> intentionList = node.getIntents().stream()
                .filter(intention -> Fc.notEqualsValue(intention.getCode(), OTHER_CODE))
                .collect(Collectors.toList());

        // 解析用户的问题
        String question = parser.parseExpression(node.getQuestion()).getValue(nodeContext.getContext(), String.class);

        // 先用正则快速匹配，如果有满足的就直接返回，没有就用模型
        List<UserIntent.Node.IntentionNode.Intention> listMatch = intentionList.stream()
                .filter(intention -> Fc.isNotBlank(intention.getQuickMatchReg()))
                .filter(intention -> matchAny(question, intention.getQuickMatchReg()))
                .collect(Collectors.toList());

        log.info("快速匹配结果[{}]：{}", question, listMatch);
        if (listMatch.size() == 1) {
            // 快速匹配成功
            UserIntent.Node.IntentionNode.Intention intention = listMatch.get(0);
            return NodeResult.builder().nextId(intention.getNextNode()).result(intention.getCode()).build();
        } else if (listMatch.size() > 1) {
            // 快速匹配多个
            intentionList = listMatch;
        }

        // 其他意图
        UserIntent.Node.IntentionNode.Intention intentionOther = node.getIntents().stream().filter(intention -> Fc.equalsValue(intention.getCode(), OTHER_CODE)).findFirst().orElseGet(()-> UserIntent.Node.IntentionNode.Intention.builder().nextNode("end").build());
        intentionOther.setCode(OTHER_CODE);
        intentionOther.setDescription(OTHER_DESCRIPTION);
        intentionOther.setQuickMatchReg(null);
        // 添加其他意图
        intentionList.add(intentionOther);

        // 拼接意图分类
        String intentions = intentionList.stream()
                .map(intention -> " - " + intention.getCode() + "：" + intention.getDescription())
                .collect(Collectors.joining(StrPool.LF));

        // 构造历史记录
        var ref = new Object() {String history = "";};
        if (Fc.isNotBlank(node.getHistory())){
            String history = parser.parseExpression(node.getHistory()).getValue(nodeContext.getContext(), String.class);
            String lastKefu = StrUtil.prependIfMissing(history, "【客服】：", "【客服】:", "【客服】：");
            ref.history = "历史记录：" + lastKefu;
        }

        try {

            // 播放等待音乐
            try {
                if (node.getPlayMusic()) {
                    if (Fc.isNotBlank(node.getMusicPrompt())){
                        nodeContext.getSupport().streamFile(node.getMusicPrompt(), false);
                    }
                    nodeContext.getSupport().playMusicOnHold(MUSIC);
                }

            } catch (AgiHangupException hangupException){
                ThreadUtil.interrupt(Thread.currentThread(), false);
                return NodeResult.builder().nextId("end").result(OTHER_CODE).build();
            }

            // 请求大模型
            Intention intent = chatClients.client()
                    .prompt()
                    .options(JpowerChatOptions.builder()
                            .temperature(0.1)
                            .maxTokens(600)
                            .jsonMode()
                            .build())
                    .system(SYSTEM_PROMPT)
                    .user(u -> u.text(PROMPT_TEMPLATE)
                            .param("intentions", intentions)
                            .param("history", ref.history)
                            .param("question", question)
                    )
                    .call()
                    .entity(Intention.class);

            var intentionOptional = intentionList.stream().filter(intention -> Fc.equalsValue(intention.getCode(), intent.intent())).findFirst();

            log.info("意图总用时={}, 识别结果={}, 用户问题={}", interval.intervalPretty(), intent, question);

            if (intentionOptional.isPresent()){
                return NodeResult.builder().result(intentionOptional.get().getCode()).nextId(intentionOptional.get().getNextNode()).build();
            } else {
                return NodeResult.builder().result(intentionOther.getCode()).nextId(intentionOther.getNextNode()).build();
            }

        } catch (Exception e){
            log.error("调用大模型报错==={}", ExceptionUtil.stacktraceToString(e));

            return NodeResult.builder().result(intentionOther.getCode()).nextId(intentionOther.getNextNode()).build();
        }

    }

    /**
     * 意图结果
     *
     * @param intent 意图名称
     * @param confidence 置信度
     */
    public record Intention(String intent, Double confidence) {}

    /**
     * 因为结构化输出不准确，所以自己写了输出转换器
     * @param <T>
     */
    public static class MrTBeanOutputConverter<T> extends BeanOutputConverter<T> {

        public MrTBeanOutputConverter(Class<T> clazz) {
            super(clazz);
        }

        /**
         * 去除<think>标签思考内容后交给框架处理
         * @param text 原始报文
         */
        @Override
        public T convert(@NonNull String text) {
            log.info("意图大模型输出==={}", text);
//            return super.convert(ReUtil.replaceAll(text, Pattern.compile("<think>.*?</think>", Pattern.DOTALL),""));
            text = text.trim();
            if (text.startsWith("json{") && text.endsWith("}")){
                text = text.substring(4);
            }
            return super.convert(text);
        }
    }

    public static boolean matchAny(String question, String rawRules) {
        List<String> regexList = safeSplitRegex(rawRules);

        for (String regex : regexList) {
            Pattern pattern = PATTERN_CACHE.computeIfAbsent(regex.trim(), key->{
                return Pattern.compile(regex.trim(), Pattern.MULTILINE);
            });
            Matcher matcher = pattern.matcher(question);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安全分割方法：按逗号分割，但忽略花括号 {} 和圆括号 () 内部的逗号
     * 解决 {0,8} 或 (a|b) 等结构被错误切断的问题
     */
    private static List<String> safeSplitRegex(String content) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0; // 记录 ( 和 { 的嵌套深度

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            // 处理转义字符，跳过下一个字符的检查
            if (c == '\\' && i + 1 < content.length()) {
                current.append(c);
                i++; // 跳过转义的字符
                current.append(content.charAt(i));
                continue;
            }

            if (c == '(' || c == '{') {
                depth++;
                current.append(c);
            } else if (c == ')' || c == '}') {
                depth--;
                current.append(c);
            } else if (c == ',' && depth == 0) {
                // 只有在所有括号外层的逗号才作为分隔符
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        // 添加最后一个
        if (current.length() > 0) {
            result.add(current.toString());
        }
        return result;
    }

}
