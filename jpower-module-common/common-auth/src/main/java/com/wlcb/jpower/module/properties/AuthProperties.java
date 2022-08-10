package com.wlcb.jpower.module.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权配置
 *
 * @author mr.g
 **/
@Data
@ConfigurationProperties("jpower.auth")
public class AuthProperties {

    /**
     * 放行API集合
     */
    private final List<String> skipUrl = new ArrayList<>();
    /**
     * 白名单集合
     */
    private List<String> whileIp = new ArrayList<>();
    /**
     * 客户端信息
     */
    private List<Client> client = new ArrayList<>();

    @Data
    public static class Client{
        /** 客户端编码 */
        private String code;
        /** 接口地址规则 */
        private List<String> path = new ArrayList<>();
    }
}
