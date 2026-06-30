package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.NotNull;
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

import java.util.Comparator;
import java.util.List;

/**
 * 条件判断
 */
@Slf4j
@Component(ConditionGranter.GRANT_TYPE)
public class ConditionGranter implements NodeGranter<UserIntent.Node.ConditionNode> {
    public static final String GRANT_TYPE = "condition";

    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ConditionNode node) {

        List<UserIntent.Node.ConditionNode.Condition> list = node.getConditions().stream().sorted(Comparator.comparingInt(UserIntent.Node.ConditionNode.Condition::getIndex)).toList();
        UserIntent.Node.ConditionNode.Condition resultCondition = list.get(list.size()-1);
        for (UserIntent.Node.ConditionNode.Condition condition : list) {
            if (Fc.isNotBlank(condition.getCondition()) && !StrUtil.equalsIgnoreCase(condition.getCondition(), "else")){
                Expression expression = parser.parseExpression(condition.getCondition());
                boolean is = expression.getValue(nodeContext.getContext(), Boolean.TYPE);
                if (is) {
                    resultCondition = condition;
                    break;
                }
            }
        }

        NodeResult.NodeResultBuilder builder = NodeResult.builder().nextId(resultCondition.getNextNode());

        if (Fc.isBlank(resultCondition.getResult())){
            return builder.build();
        }

        Expression expression = parser.parseExpression(resultCondition.getResult());
        Object result = expression.getValue(nodeContext.getContext());
        return builder.result(resultIfNull(result)).build();
    }

    private Object resultIfNull(Object result){
        if (result instanceof String){
            if (StrUtil.isBlank((String) result) || Fc.equalsValue(result, "null")){
                return null;
            }
        }
        return result;
    }

}
