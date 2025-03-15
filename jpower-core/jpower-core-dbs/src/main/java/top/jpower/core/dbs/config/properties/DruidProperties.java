package top.jpower.core.dbs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jpower.druid")
public class DruidProperties {

    /**
     * 白名单
     */
    private String allow;

    /**
     * 黑名单
     */
    private String deny;

    /**
     * web登录用户名
     */
    private String loginUsername;

    /**
     * web登录密码
     */
    private String loginPassword;

    /**
     * 是否可以重置数据源
     */
    private Boolean resetEnable;

}
