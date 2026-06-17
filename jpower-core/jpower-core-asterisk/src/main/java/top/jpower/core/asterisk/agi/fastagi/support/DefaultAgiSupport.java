package top.jpower.core.asterisk.agi.fastagi.support;

import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.util.utils.SpringUtil;

/**
 * 默认实现类
 *
 * @author mr.g
 */
@Slf4j
public class DefaultAgiSupport extends AgiSupport {

    public DefaultAgiSupport(AgiChannel channel, AgiRequest request, Thread thread) {
        super(channel, request, thread);
    }

    @Override
    protected void realtimeData(Boolean isBot, String filePath, String content) { }

    /**
     * 初始化asr
     *
     * @return asrClient
     */
    @Override
    protected AsrClient initAsr() {
        if (SpringUtil.isExistBean(AsrClient.class)) {
            return SpringUtil.getBean(AsrClient.class);
        }
        throw new RuntimeException("没有找到AsrClient Bean, 请实现top.jpower.core.asterisk.audio.AsrClient接口");
    }

    /**
     * 初始化tts
     *
     * @return ttsClient
     */
    @Override
    protected TtsClient initTts() {
        if (SpringUtil.isExistBean(TtsClient.class)) {
            return SpringUtil.getBean(TtsClient.class);
        }
        throw new RuntimeException("没有找到TtsClient Bean, 请实现top.jpower.core.asterisk.audio.TtsClient接口");
    }
}
