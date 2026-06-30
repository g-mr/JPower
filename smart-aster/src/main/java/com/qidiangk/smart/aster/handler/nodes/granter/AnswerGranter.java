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
 * 收取录音
 */
@Slf4j
@Component(AnswerGranter.GRANT_TYPE)
public class AnswerGranter implements NodeGranter<UserIntent.Node.AnswerNode> {
    public static final String GRANT_TYPE = "answer";
    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.AnswerNode node) {

        String audio = "";
        if (Fc.isNotBlank(node.getJqrask())){
            Expression expression = parser.parseExpression(node.getJqrask());
            audio = expression.getValue(nodeContext.getContext(), String.class);
            nodeContext.put(IVR_LAST_MSG, audio);
        }

        String result = nodeContext.getSupport().radio(audio, node.getIsSave());
        return NodeResult.builder()
                .nextId(node.getNextNode())
                .result(ObjectUtil.defaultIfNull(result, StringPool.EMPTY))
                .build();
    }

}
