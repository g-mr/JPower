package top.jpower.core.util.support.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮件配置属性，从 application.yml 中读取 jpower.mail 前缀的配置
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.mail")
public class MailProperties {

    /**
     * SMTP服务器地址
     */
    private String host;

    /**
     * SMTP服务器端口
     */
    private Integer port;

    /**
     * 发件人邮箱地址
     */
    private String from;

    /**
     * 用户名（通常为邮箱地址）
     */
    private String user;

    /**
     * 密码或授权码
     */
    private String pass;

    /**
     * 是否使用SSL
     */
    private Boolean sslEnable = true;

}
