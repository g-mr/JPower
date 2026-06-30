package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.util.ObjectUtil;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.utils.AgiContext;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;

/**
 * 变量赋值
 */
@Slf4j
@Component(GlobeValueGranter.GRANT_TYPE)
public class GlobeValueGranter implements NodeGranter<UserIntent.Node.GlobeValueNode> {

    public static final String GRANT_TYPE = "variable-assign";

    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.GlobeValueNode node) {

        node.getVariables().forEach(variable -> {
            Object val = parser.parseExpression(variable.getVal()).getValue(nodeContext.getContext());
            AgiContext.cache(nodeContext.getSupport().channel()).put(variable.getKey(), val);
            nodeContext.put(variable.getKey(), val);
        });

        if (Fc.isNotBlank(node.getResult())) {
            Expression expression = parser.parseExpression(node.getResult());
            return NodeResult.builder()
                    .nextId(node.getNextNode())
                    .result(ObjectUtil.defaultIfNull(expression.getValue(nodeContext.getContext()), StringPool.EMPTY))
                    .build();
        }

        return NodeResult.builder().nextId(node.getNextNode()).build();
    }

}
