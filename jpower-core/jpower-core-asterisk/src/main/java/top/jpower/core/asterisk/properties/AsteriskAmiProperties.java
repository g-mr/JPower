package top.jpower.core.asterisk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "asterisk.ami")
@Data
public class AsteriskAmiProperties {
    /**
     * AMI 地址
     */
    private String host;
    /**
     * AMI 端口号
     */
    private Integer port;
    /**
     * AMI 用户名
     */
    private String username;
    /**
     * AMI 密码
     */
    private String password;

    /**
     * 是否执行监听事件
     */
    private Boolean event = false;

}
