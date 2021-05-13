package com.wlcb.jpower.module.configurer.client;

import com.wlcb.jpower.module.properties.AuthProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName AuthConfigurer
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:19
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class ClientConfigurer implements WebMvcConfigurer {

    private AuthProperties authProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<String> set = new HashSet<>();
        List<AuthProperties.Client> clients = authProperties.getClient();
        clients.forEach(client -> set.addAll(client.getPath()));
        if (clients.size() > 0){
            registry.addInterceptor(new ClientInterceptor(clients)).addPathPatterns(new ArrayList<>(set));
        }
    }

}
