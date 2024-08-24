package top.jpower.core.boot.argument;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.SpringProperties;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册参数解析器
 *
 * @author mr.g
 * @date 2024/3/9 3:58 PM
 */
@AutoConfiguration
public class SingleBodyConfig implements WebMvcConfigurer {

    public List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(4);
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        if (!SpringProperties.getFlag("spring.xml.ignore")) {
            try {
                messageConverters.add(new SourceHttpMessageConverter<>());
            }
            catch (Error ignored) {}
        }
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        return messageConverters;
    }

    /**
     * 注册自定义的参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(new RequestBodyHandlerMethodArgumentResolver(getMessageConverters(),
                new ContentNegotiationManager()));
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

}
