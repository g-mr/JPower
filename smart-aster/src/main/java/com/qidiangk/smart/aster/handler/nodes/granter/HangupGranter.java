package com.qidiangk.smart.aster.handler.nodes.granter;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;

/**
 * 挂断
 */
@Slf4j
@Component(HangupGranter.GRANT_TYPE)
public class HangupGranter implements NodeGranter<UserIntent.Node.HangupNode> {

    public static final String GRANT_TYPE = "hangup";

    @SneakyThrows
    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.HangupNode node) {
        if (Fc.isNotBlank(node.getMessage())){
            nodeContext.getSupport().streamFile(node.getMessage(), false);
        }
        nodeContext.getSupport().hangup();
        return NodeResult.builder().nextId(node.getNextNode()).build();
    }
}
