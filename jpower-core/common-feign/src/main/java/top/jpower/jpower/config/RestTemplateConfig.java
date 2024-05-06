package top.jpower.jpower.config;

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
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.jpower.module.common.auth.SecureConstant;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
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
        httpRequestFactory.setHttpClient(httpClientBuilder().build());
        //获取链接超时时间
        httpRequestFactory.setConnectionRequestTimeout(3000);
        //指客户端和服务器建立连接的timeout
        httpRequestFactory.setConnectTimeout(3000);
        //读取数据的超时时间
        httpRequestFactory.setReadTimeout(120000);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

        restTemplate.getInterceptors().add((request, body, execution) -> {

            Enumeration<String> headerNames = WebUtil.getRequest().getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (name.equalsIgnoreCase(SecureConstant.BASIC_HEADER_KEY)
                            || name.equalsIgnoreCase("User-Type")
                            || name.equalsIgnoreCase(JpowerConstants.HEADER_MENU)
                            || name.equalsIgnoreCase(JpowerConstants.AUTH_HEADER)
                            || name.equalsIgnoreCase(JpowerConstants.HEADER_TENANT)
                            || name.equalsIgnoreCase(TokenConstant.DATA_SCOPE_NAME)
                            || name.equalsIgnoreCase(TokenConstant.PASS_HEADER_NAME)){

                        String values = WebUtil.getRequest().getHeader(name);
                        request.getHeaders().add(name, values);

                    }
                }
            }


            return execution.execute(request, body);
        });


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

    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //设置HTTP连接管理器
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }

    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数
        poolingConnectionManager.setMaxTotal(1000);
        // 每个主机的并发
        poolingConnectionManager.setDefaultMaxPerRoute(500);
        return poolingConnectionManager;
    }

}
