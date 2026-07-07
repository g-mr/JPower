package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.maxkb.api.feign.AgentChatClient;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.asteriskjava.fastagi.AgiHangupException;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;

import static com.qidiangk.smart.aster.constants.ConstantUtil.MUSIC;

/**
 * 智能体会话
 *
 * @author mr.g
 */
@Slf4j
@Component(MaxKBGranter.GRANT_TYPE)
@RequiredArgsConstructor
public class MaxKBGranter implements NodeGranter<UserIntent.Node.MaxKBNode> {

    public static final String GRANT_TYPE = "maxKB";

    private final AgentChatClient chatClient;

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.MaxKBNode node) {
        NodeResult.NodeResultBuilder builder = NodeResult.builder().nextId(node.getNextNode());

        TimeInterval interval =  DateUtil.timer();
        try {
            if (node.isWaitMusic()) {
                nodeContext.getSupport().playMusicOnHold(MUSIC);
            }
        } catch (AgiHangupException hangupException){
            ThreadUtil.interrupt(Thread.currentThread(), false);
            return NodeResult.builder().nextId("end").build();
        }

        String issue = StringSubstitutor.replace(node.getIssue(), nodeContext.getParams());


        try {
            R<String> r = chatClient.message(nodeContext.getCallerNum(), node.getAgent(), issue, node.isReChat());
            if (r.isStatus()) {
                log.info("智能体回答速度==={}=====提问问题={}======返回结果={}", interval.intervalPretty(), issue, r.getData());
                return builder.result(r.getData()).build();
            } else {
                log.error("调用智能体异常==>{}", JSONUtil.toJsonPrettyStr(r));
                return builder.result(node.getFailResult()).build();
            }
        } catch (Exception e) {
            log.error("调用智能体报错==>{}", ExceptionUtil.stacktraceToString(e));
            return builder.result(node.getFailResult()).build();
        }

    }

}
