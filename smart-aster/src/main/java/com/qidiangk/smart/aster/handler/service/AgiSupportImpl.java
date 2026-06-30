package com.qidiangk.smart.aster.handler.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.pojo.dto.ActionDTO;
import com.qidiangk.smart.aster.service.ExteriorService;

/**
 * Agi交互逻辑实现类
 *
 * @author mr.g
 */
@Slf4j
public class AgiSupportImpl extends AgiSupport {

    private final ExteriorService exteriorService;

    public AgiSupportImpl(AgiChannel channel, AgiRequest request, AsrClient asrClient, TtsClient ttsClient, Thread thread) {
        super(channel, request, asrClient, ttsClient, thread);
        exteriorService = SpringUtil.getBean(ExteriorService.class);
    }

    @Override
    protected void realtimeData(Boolean isBot, String filePath, String content) {
        // 传递实时会话
        try {
            exteriorService.sendAction(ActionDTO.builder()
                    .type(isBot?1:0)
                    .callId(getUniqueId())
                    .content(content)
                    .recordBase64(Fc.isBlank(filePath) ? null : Base64.encode(FileUtil.readBytes(filePath)))
                    .build());
        } catch (Exception e){
            log.error("实时会话传递失败===>>{}", e.getMessage());
        }
    }
}
