package com.qidiangk.smart.maxkb.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.lianjiatech.retrofit.spring.boot.config.RetrofitAutoConfiguration;
import com.github.lianjiatech.retrofit.spring.boot.core.DefaultBaseUrlParser;
import com.github.lianjiatech.retrofit.spring.boot.core.ErrorDecoder;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import com.qidiangk.smart.maxkb.config.property.MaxKBProperty;
import com.qidiangk.smart.maxkb.config.retrofit.BodyCodeConverterFactory;
import com.qidiangk.smart.maxkb.config.retrofit.HttpErrorDecoder;
import com.qidiangk.smart.maxkb.config.retrofit.PathBaseUrlParser;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author mr.g
 */
@Configuration
@AutoConfigureBefore(RetrofitAutoConfiguration.class)
@EnableConfigurationProperties(MaxKBProperty.class)
@RetrofitScan("com.qidiangk.smart.maxkb.client")
public class RetrofitConfiguration {

    @Bean
    public DefaultBaseUrlParser defaultBaseUrlParser(){
        return new PathBaseUrlParser();
    }

    @Bean
    public JacksonConverterFactory retrofitJacksonConverterFactory(ObjectMapper objectMapper) {
        ObjectMapper ob = objectMapper.copy();
        ob.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        ob.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return JacksonConverterFactory.create(ob);
    }

    @Bean
    public ErrorDecoder.DefaultErrorDecoder retrofitDefaultErrorDecoder() {
        return new HttpErrorDecoder();
    }

    @Bean
    public BodyCodeConverterFactory bodyCodeConverterFactory(){
        return new BodyCodeConverterFactory();
    }

}
