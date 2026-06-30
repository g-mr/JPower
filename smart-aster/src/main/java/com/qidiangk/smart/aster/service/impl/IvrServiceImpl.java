package com.qidiangk.smart.aster.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.handler.nodes.NodeGranterFactory;
import com.qidiangk.smart.aster.handler.nodes.NodeState;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.service.IvrService;
import com.qidiangk.smart.aster.utils.StrNumber;

import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class IvrServiceImpl implements IvrService {

    private final NodeGranterFactory nodeGranterFactory;
    @Lookup
    protected abstract NodeState createNodeState(List<? extends UserIntent.Node> nodes, NodeGranterFactory nodeGranterFactory);

    @Override
    public String executeNode(final AgiSupport support, List<? extends UserIntent.Node> nodes) {
        NodeState nodeState = createNodeState(nodes, nodeGranterFactory);
        return StrNumber.coverChinese(StrUtil.removeAny(StrUtil.cleanBlank(Fc.toStr(nodeState.proceed(support))), "*", "-","/","\\"), Boolean.TRUE);
    }

}
