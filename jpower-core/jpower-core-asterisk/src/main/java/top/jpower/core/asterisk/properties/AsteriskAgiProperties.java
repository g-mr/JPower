package top.jpower.core.asterisk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

}
