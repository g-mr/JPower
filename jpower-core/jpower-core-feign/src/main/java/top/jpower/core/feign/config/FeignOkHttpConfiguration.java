package top.jpower.core.feign.config;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.net.DefaultTrustManager;
import cn.hutool.core.net.SSLUtil;
import cn.hutool.http.ssl.DefaultSSLInfo;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import top.jpower.core.feign.config.interceptor.HttpLogInterceptor;
import top.jpower.core.feign.config.properties.FeignHttpProperties;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.net.SSLProtocols.SSL;

/**
 * 配置OkHttpClient
 *
 * @author mr.g
 * @date 2021-05-21 14:33
 */
@AutoConfiguration
@AutoConfigureBefore(FeignLoadBalancerAutoConfiguration.class)
@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
@EnableConfigurationProperties(FeignHttpProperties.class)
@Slf4j
public class FeignOkHttpConfiguration {

    private okhttp3.OkHttpClient okHttpClient;

    @Bean
    public HttpLogInterceptor httpLogInterceptor(FeignHttpProperties httpProperties) {
        return new HttpLogInterceptor(httpProperties.getLogLevel());
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool httpClientConnectionPool(FeignHttpProperties httpProperties) {
        return new ConnectionPool(httpProperties.getMaxConnections(),
                httpProperties.getTimeToLive(),
                httpProperties.getTimeToLiveUnit());
    }

    @Bean
    public okhttp3.OkHttpClient client(ConnectionPool connectionPool,
                                       FeignHttpProperties httpProperties,
                                       HttpLogInterceptor logInterceptor) {
        this.okHttpClient = createBuilder(httpProperties.isDisableSslValidation())
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

    private OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (disableSslValidation) {
            try {
                X509TrustManager disabledTrustManager = DefaultTrustManager.INSTANCE;
                SSLSocketFactory sslSocketFactory = SSLUtil.createSSLContext(SSL, null, disabledTrustManager).getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, disabledTrustManager);
                builder.hostnameVerifier(DefaultSSLInfo.TRUST_ANY_HOSTNAME_VERIFIER);
            } catch (IORuntimeException e) {
                log.warn("在OKHttpClient中设置SSLSocketFactory时出错", e);
            }
        }
        return builder;
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }

}
