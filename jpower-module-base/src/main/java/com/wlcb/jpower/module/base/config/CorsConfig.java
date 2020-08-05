package com.wlcb.jpower.module.base.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("PUT","GET","POST","DELETE","OPTIONS")
//                .allowedOrigins("*")
//                .allowCredentials(true);
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("PUT","GET","POST","DELETE","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("appId","x-requested-with","openid")
                .maxAge(3600);
    }

}
