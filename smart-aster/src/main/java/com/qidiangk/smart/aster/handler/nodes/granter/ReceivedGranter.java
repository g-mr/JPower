package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.util.ObjectUtil;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;

/**
 * 收号
 */
@Slf4j
@Component(ReceivedGranter.GRANT_TYPE)
public class ReceivedGranter implements NodeGranter<UserIntent.Node.ReceivedNode> {
    public static final String GRANT_TYPE = "received";
    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ReceivedNode node) {

        Expression expression = parser.parseExpression(node.getJqrask());
        String audio = expression.getValue(nodeContext.getContext(), String.class);

        String result = nodeContext.getSupport().received(audio, node.getEnd(), node.getIsSave());

        return NodeResult.builder().nextId(node.getNextNode()).result(ObjectUtil.defaultIfNull(result, StringPool.EMPTY)).build();
    }

}
