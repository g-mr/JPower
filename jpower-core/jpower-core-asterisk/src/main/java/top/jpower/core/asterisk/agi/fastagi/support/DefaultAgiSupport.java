package top.jpower.core.asterisk.agi.fastagi.support;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;

/**
 * 默认实现类
 */
public class DefaultAgiSupport extends AgiSupport {

    public DefaultAgiSupport(AgiChannel channel, AgiRequest request, AsrClient asrClient, TtsClient ttsClient, Thread thread) {
        super(channel, request, asrClient, ttsClient, thread);
    }

    @Override
    protected void realtimeData(Boolean isBot, String filePath, String content) {

    }
}
