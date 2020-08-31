package com.wlcb.jpower.module.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ClientProperties
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:22
 * @Version 1.0
 */
@Data
@ConfigurationProperties("jpower.auth")
public class AuthProperties {

    private List<String> whileIp = new ArrayList<>();
    private List<Client> client = new ArrayList<>();

    @Data
    public static class Client{
        private String code;
        private List<String> path = new ArrayList<>();
    }
}
