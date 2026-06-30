package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alinlp.model.v20200629.GetSaChGeneralRequest;
import com.aliyuncs.alinlp.model.v20200629.GetSaChGeneralResponse;
import com.aliyuncs.profile.DefaultProfile;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;

/**
 * 情绪判断
 * <br/>
 * <a href="https://help.aliyun.com/zh/document_detail/609364.html?spm=a2c4g.11186623.0.0.7b5e3e1dLx4d2a">接口地址</a>
 */
@Slf4j
@Component(SentimentGranter.GRANT_TYPE)
@RequiredArgsConstructor
public class SentimentGranter implements NodeGranter<UserIntent.Node.SentimentNode> {
    public static final String GRANT_TYPE = "sentiment";

    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();
    private final AliProperty aliProperty;

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.SentimentNode node) {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                "cn-hangzhou",
                aliProperty.getAccessKeyId(),
                aliProperty.getAccessKeySecret());

        IAcsClient client = new DefaultAcsClient(defaultProfile);
        GetSaChGeneralRequest request = new GetSaChGeneralRequest();
        request.setSysEndpoint("alinlp.cn-hangzhou.aliyuncs.com");
        request.setServiceCode("alinlp");

        Expression expression = parser.parseExpression(node.getMessage());
        String message = expression.getValue(nodeContext.getContext(), String.class);
        request.setText(message);

        JSONObject json = new JSONObject();
        try {
            GetSaChGeneralResponse response = client.getAcsResponse(request);
            String jsr = response.getData();
            log.info("情绪接口返回=={}", jsr);
            json = JSON.parseObject(jsr);
        } catch (Exception e){
            log.error("情绪接口请求报错==={}", ExceptionUtil.stacktraceToString(e));
        }

        if (json.getBooleanValue("success")){
            if (Fc.equalsValue("正面", json.getJSONObject("result").getString("sentiment"))) {
                UserIntent.Node.SentimentNode.Front front = node.getFront();
                return NodeResult.builder()
                        .nextId(front.getNextNode())
                        .result(ObjectUtil.defaultIfNull(parser.parseExpression(front.getResult()).getValue(nodeContext.getContext()), StringPool.EMPTY))
                        .build();
            } else if (Fc.equalsValue("负面", json.getJSONObject("result").getString("sentiment"))){
                UserIntent.Node.SentimentNode.Negative negative = node.getNegative();
                return NodeResult.builder()
                        .nextId(negative.getNextNode())
                        .result(ObjectUtil.defaultIfNull(parser.parseExpression(negative.getResult()).getValue(nodeContext.getContext()), StringPool.EMPTY))
                        .build();
            }
        }

        UserIntent.Node.SentimentNode.Neuter neuter = node.getNeuter();
        return NodeResult.builder()
                .nextId(neuter.getNextNode())
                .result(ObjectUtil.defaultIfNull(parser.parseExpression(neuter.getResult()).getValue(nodeContext.getContext()), StringPool.EMPTY))
                .build();
    }

}
