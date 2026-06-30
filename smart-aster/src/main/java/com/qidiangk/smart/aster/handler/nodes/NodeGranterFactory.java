package com.qidiangk.smart.aster.handler.nodes;

import org.springframework.stereotype.Component;
import com.qidiangk.smart.aster.constants.TypeEnum;
import com.qidiangk.smart.aster.pojo.UserIntent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NodeGranterFactory {

    /**
     * TokenGranter缓存池
     */
    private final Map<String, NodeGranter> granterPool = new ConcurrentHashMap<>();

    public NodeGranterFactory(Map<String, NodeGranter<? extends UserIntent.Node>> granterPool) {
        this.granterPool.putAll(granterPool);
    }

    /**
     * 获取NodeGranter
     *
     * @param grantType 类型
     * @return TokenGranter
     */
    public NodeGranter<UserIntent.Node> getGranter(TypeEnum grantType) {
        return granterPool.get(grantType.toValue());
    }

}
