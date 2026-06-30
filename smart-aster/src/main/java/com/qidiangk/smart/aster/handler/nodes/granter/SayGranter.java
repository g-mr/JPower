package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.util.ObjectUtil;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
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

import static com.qidiangk.smart.aster.constants.ConstantUtil.IVR_LAST_MSG;

/**
 * 播放内容
 */
@Slf4j
@Component(SayGranter.GRANT_TYPE)
public class SayGranter implements NodeGranter<UserIntent.Node.SayNode> {
    public static final String GRANT_TYPE = "say";
    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.SayNode node) {

        Expression expression = parser.parseExpression(node.getPlay());
        String play = expression.getValue(nodeContext.getContext(), String.class);
        nodeContext.getSupport().streamFile(play, node.getInterrupt(), node.getIsSave());
        nodeContext.put(IVR_LAST_MSG, play);

        NodeResult.NodeResultBuilder builder = NodeResult.builder().nextId(node.getNextNode());
        if (Fc.isBlank(node.getResult())){
            return builder.build();
        }
        return builder.result(ObjectUtil.defaultIfNull(parser.parseExpression(node.getResult()).getValue(nodeContext.getContext(), String.class), StringPool.EMPTY)).build();
    }

}
