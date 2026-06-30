package com.qidiangk.smart.maxkb.config.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.DefaultBaseUrlParser;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import top.jpower.core.util.utils.Fc;

import java.util.Objects;

public class PathBaseUrlParser extends DefaultBaseUrlParser {

    String SUFFIX = "/";
    String HTTP_PREFIX = "http://";

    @Override
    public String parse(RetrofitClient retrofitClient, Environment environment) {
        String baseUrl = Objects.requireNonNull(retrofitClient).baseUrl();
        if (StringUtils.hasText(baseUrl)) {
            baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
            // 解析baseUrl占位符
            if (!baseUrl.endsWith(SUFFIX)) {
                baseUrl += SUFFIX;
            }

            String path = retrofitClient.path();
            if (Fc.isNotBlank(path)){
                if (!path.endsWith(SUFFIX)) {
                    path += SUFFIX;
                }

                baseUrl += path;
            }
        } else {
            String serviceId = retrofitClient.serviceId();
            String path = retrofitClient.path();
            if (!path.endsWith(SUFFIX)) {
                path += SUFFIX;
            }
            baseUrl = HTTP_PREFIX + (serviceId + SUFFIX + path).replaceAll("/+", SUFFIX);
            baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
        }
        return baseUrl;
    }
}
