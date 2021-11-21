package com.wlcb.jpower.module.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DemoProperties
 * @Description TODO 演示环境配置
 * @Author 郭丁志
 * @Date 2020-10-14 21:12
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jpower.demo")
public class DemoProperties {

    /** 是否开启 **/
    private boolean enable = false;

    /** 不拦截的接口 **/
    private List<String> skipUrl = new ArrayList<>();
}
