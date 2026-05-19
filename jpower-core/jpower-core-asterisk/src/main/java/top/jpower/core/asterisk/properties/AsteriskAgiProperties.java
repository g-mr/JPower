package top.jpower.core.asterisk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;

@ConfigurationProperties(prefix = "asterisk.agi")
@Data
public class AsteriskAgiProperties {

    /**
     * AGI 端口号
     */
    private Integer port;
    /**
     * 线程池大小
     */
    private Integer poolSize;
    /**
     * 最大线程池
     */
    private Integer maxPoolSize;
    /**
     * ASR实现类
     */
    private Class<? extends AsrClient> asrImpl;
    /**
     * TTS实现类
     */
    private Class<? extends TtsClient> ttsImpl;

}
