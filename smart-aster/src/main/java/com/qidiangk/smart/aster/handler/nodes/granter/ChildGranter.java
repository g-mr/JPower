package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.map.MapBuilder;
import com.qidiangk.smart.aster.handler.nodes.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.handler.nodes.*;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.service.ICallRouteService;

import java.util.List;
import java.util.Map;

import static com.qidiangk.smart.aster.constants.ConstantUtil.*;

/**
 * 子流程
 * @author mr.gmac
 */
@Slf4j
@Component(ChildGranter.GRANT_TYPE)
public class ChildGranter implements NodeGranter<UserIntent.Node.ChildNodes> {

    public static final String GRANT_TYPE = "childNodes";


    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Autowired
    private ICallRouteService callRouteService;

    @Lazy
    @Autowired
    private NodeGranterFactory nodeGranterFactory;

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ChildNodes node) {

        List<? extends UserIntent.Node> list = callRouteService.findCompleteFlow(node.getIntentId());
        if (Fc.isNotEmpty(list)){
            Map<String, Object> context = MapBuilder.<String, Object>create()
                    .put(IVR_LAST_RESULT, lastResult)
                    .put(IVR_LAST_MSG, nodeContext.getParams().get(IVR_LAST_MSG))
                    .put(IVR_ISSUE, parser.parseExpression(node.getIssue()).getValue(nodeContext.getContext())).build();

            NodeState nodeState = new NodeState(list, nodeGranterFactory);
            Object result = nodeState.proceed(nodeContext.getSupport(), context);

            log.info("子流程返回结果===>>{}", result);

            return NodeResult
                    .builder()
                    .nextId(node.getNextNode())
                    .result(result)
                    .build();
        }

        return NodeResult.builder().nextId(node.getNextNode()).build();
    }
}
