package com.wlcb.jpower.feign.config;

import com.wlcb.jpower.feign.interceptor.HttpLogInterceptor;
import com.wlcb.jpower.feign.properties.FeignHttpProperties;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 重写 {@link org.springframework.cloud.openfeign.clientconfig.OkHttpFeignConfiguration}
 * @author mr.g
 * @date 2021-05-21 14:33
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
@ComponentScan(basePackageClasses = HttpClientConfiguration.class)
@EnableConfigurationProperties(FeignHttpProperties.class)
public class FeignOkHttpConfiguration {

    private okhttp3.OkHttpClient okHttpClient;

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool httpClientConnectionPool(FeignHttpProperties httpProperties,
                                                   OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        return connectionPoolFactory.create(httpProperties.getMaxConnections(),
                httpProperties.getTimeToLive(),
                httpProperties.getTimeToLiveUnit());
    }

    @Bean
    public HttpLogInterceptor httpLogInterceptor(FeignHttpProperties httpProperties) {
        return new HttpLogInterceptor(httpProperties.getLogLevel());
    }

    @Bean
    public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory,
                                       ConnectionPool connectionPool,
                                       FeignHttpProperties httpProperties,
                                       HttpLogInterceptor logInterceptor) {
        this.okHttpClient = httpClientFactory.createBuilder(httpProperties.isDisableSslValidation())
                .connectTimeout(httpProperties.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .followRedirects(httpProperties.isFollowRedirects())
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(httpProperties.isRetryOnConnectionFailure())
                .callTimeout(httpProperties.getCallTimeout(),TimeUnit.MILLISECONDS)
                .readTimeout(httpProperties.getReadTimeout(),TimeUnit.MILLISECONDS)
                .writeTimeout(httpProperties.getWriteTimeout(),TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(logInterceptor)
                .build();
        return this.okHttpClient;
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }

}
