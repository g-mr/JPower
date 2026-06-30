package com.qidiangk.smart.aster.service;

import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import com.qidiangk.smart.aster.pojo.UserIntent;

import java.util.List;

public interface IvrService {
    String executeNode(final AgiSupport support, List<? extends UserIntent.Node> nodes);
}
