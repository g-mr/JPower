package top.jpower.core.asterisk.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "asterisk")
public class AsteriskProperties {

    /**
     * SIP传输配置的ID
     */
    private String transport;
    /**
     * 音频存储根目录
     */
    private String voiceRootDir = "/data/";
    /**
     * 上下文
     */
    private Context context = new Context();

    @Data
    public static class Context {
        /**
         * 呼出上下文名称
         */
        private String out;
        /**
         * 呼入上下文名称
         */
        private String in;

        /**
         * 机器人的exten
         */
        private String exten = "robot";
    }

    public String getVoiceRootDir() {
        return StrUtil.appendIfMissing(voiceRootDir, "/");
    }

}
