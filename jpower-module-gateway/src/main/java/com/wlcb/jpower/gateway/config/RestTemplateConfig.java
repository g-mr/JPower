package com.wlcb.jpower.gateway.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author mr.g
 * @date 2022-04-22 09:31
 */

@Slf4j
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(HttpClientBuilder.create().setConnectionManager(poolingConnectionManager()).build());
        //获取链接超时时间
        httpRequestFactory.setConnectionRequestTimeout(3000);
        //指客户端和服务器建立连接的timeout
        httpRequestFactory.setConnectTimeout(3000);
        //读取数据的超时时间
        httpRequestFactory.setReadTimeout(20000);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        //重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (StringHttpMessageConverter.class == item.getClass()) {
                converterTarget = item;
                break;
            }
        }
        if (null != converterTarget) {
            converterList.remove(converterTarget);
        }
        converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        //加入FastJson转换器
        converterList.add(new FastJsonHttpMessageConverter());

        return restTemplate;
    }

    private HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数
        poolingConnectionManager.setMaxTotal(1000);
        // 每个主机的并发
        poolingConnectionManager.setDefaultMaxPerRoute(500);
        return poolingConnectionManager;
    }

}
