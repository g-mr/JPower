package top.jpower.core.dbs.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示环境配置
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.demo")
public class DemoProperties {

    /** 是否开启 **/
    private boolean enable = false;

    /** 不拦截的接口 **/
    private List<String> skipUrl = new ArrayList<>();
}
