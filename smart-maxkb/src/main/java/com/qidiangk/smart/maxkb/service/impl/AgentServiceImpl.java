package com.qidiangk.smart.maxkb.service.impl;

import com.qidiangk.smart.maxkb.client.AgentClient;
import com.qidiangk.smart.maxkb.client.po.AgentSearchBO;
import com.qidiangk.smart.maxkb.pojo.vo.SelectVO;
import com.qidiangk.smart.maxkb.service.IAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 智能体服务
 *
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements IAgentService {

    private final AgentClient agentClient;


    @Override
    public List<SelectVO> select() {
        return agentClient.select(AgentSearchBO.builder()
                        .publishStatus("published")
                        .build().toMap());
    }
}
