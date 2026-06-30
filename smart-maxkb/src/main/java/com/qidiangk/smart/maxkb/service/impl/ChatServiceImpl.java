package com.qidiangk.smart.maxkb.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.qidiangk.smart.maxkb.client.AgentClient;
import com.qidiangk.smart.maxkb.client.ChatApiClient;
import com.qidiangk.smart.maxkb.client.po.PageVO;
import com.qidiangk.smart.maxkb.client.po.agent.ApplicationKeyDO;
import com.qidiangk.smart.maxkb.client.po.client.ChatBO;
import com.qidiangk.smart.maxkb.client.po.client.MessageDO;
import com.qidiangk.smart.maxkb.service.IChatService;
import jodd.util.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 聊天服务实现
 *
 * @author mr.g
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private static final String KEY_TYPE = "Bearer ";
    private static final TimedCache<String, String> CHAT_ID_CACHE = CacheUtil.newTimedCache(1000 * 60 * 10);
    private static final TimedCache<String, String> API_KEY_CACHE = CacheUtil.newTimedCache(1000 * 60 * 10);

    private final AgentClient agentClient;
    private final ChatApiClient chatClient;

    @Override
    public String chat(String phone, String id, String issue, Boolean reChat) {
        String chatId = getChatId(phone+":"+id, id);

        MessageDO messageDO = chatClient.chatMessage(getApiKey(id), chatId, ChatBO.builder()
                .message(issue)
                .reChat(reChat)
                .stream(false)
                .build());
        log.debug("智能体回复=====提问问题={}======返回结果={}", issue, UnicodeUtil.toString(messageDO.getContent()));
        return ObjectUtil.defaultIfNull(StrUtil.removeAny(StrUtil.cleanBlank(UnicodeUtil.toString(messageDO.getContent())), "*"), StringPool.EMPTY);
    }

    /**
     * 获取chatId
     *
     * @param cacheKey 缓存key
     * @param id       id
     * @return chatId
     */
    private String getChatId(String cacheKey, String id) {
        return CHAT_ID_CACHE.get(cacheKey, ()->{
            return chatClient.chatId(getApiKey(id));
        });
    }

    /**
     * 获取apiKey
     *
     * @param id id
     * @return apiKey
     */
    private String getApiKey(String id) {
        return API_KEY_CACHE.get(id, ()->{
            PageVO<ApplicationKeyDO> pageVO = agentClient.apiKeyList(id);
            Optional<ApplicationKeyDO> optional = (pageVO.getRecords() == null ? new ArrayList<ApplicationKeyDO>() : pageVO.getRecords()).stream().filter(ApplicationKeyDO::getIsActive).filter(ApplicationKeyDO::getIsPermanent).findFirst();
            ApplicationKeyDO applicationKeyDO = optional.orElseGet(() -> agentClient.createApiKey(id));
            return KEY_TYPE + applicationKeyDO.getSecretKey();
        });
    }

}
