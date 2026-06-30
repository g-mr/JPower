package com.qidiangk.smart.aster.handler.nodes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.pojo.UserIntent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Scope("prototype")
public class NodeState {

    private final NodeGranterFactory nodeGranterFactory;

    private UserIntent.Node currentNode;
    private final Map<String, ? extends UserIntent.Node> nodeMap;

    public NodeState(List<? extends UserIntent.Node> nodeList, NodeGranterFactory nodeGranterFactory) {
        this.nodeGranterFactory = nodeGranterFactory;

        this.currentNode = nodeList.get(0);
        this.nodeMap = nodeList.stream()
                .collect(Collectors.toMap(UserIntent.Node::getCode, node -> node));
    }

    public Object proceed(final AgiSupport support, Map<String, Object> initContext) {
        NodeResult result = NodeResult.builder().build();

        NodeContext nodeContext = new NodeContext(support);
        if (Fc.isNotEmpty(initContext)){
            initContext.forEach(nodeContext::put);
        }

        while (currentNode != null && !Thread.currentThread().isInterrupted()) {
            NodeGranter<UserIntent.Node> granter = nodeGranterFactory.getGranter(currentNode.getType());
            result = granter.grant(nodeContext, result.result(), currentNode);

            // 刷新上下文
            String key = currentNode.getCode();
            nodeContext.flushed(key, result.result());

            // 获取下一个节点
            currentNode = nodeMap.get(result.nextId());
            if (currentNode == null){
                log.info("未查找到下一个节点，当前流程结束，code={},nextId={}", key, result.nextId());
            }

        }

        return result.result();
    }

    public Object proceed(final AgiSupport support) {
        return proceed(support, null);
    }
}
