package top.jpower.jpower.module.common.jackson;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import top.jpower.jpower.module.common.support.ChainMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java 8 时间默认序列化
 *
 * @author mr.g
 **/
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JpowerJacksonConfig{


    /**
     * 避免form提交context-type不规范中文乱码
     * @return Filter
     */
    @Bean
    public OrderedCharacterEncodingFilter characterEncodingFilter() {
        OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }

    /**
     * 日期格式化
     *
     * @author mr.g
     * @param
     * @return top.jpower.jpower.module.common.jackson.DateJacksonConverter
     **/
    @Bean
    @ConditionalOnMissingBean(DateJacksonConverter.class)
    public DateJacksonConverter dateJacksonConverter() {
        return new DateJacksonConverter();
    }

    /**
     * 16位或以上long转String
     *
     * @author mr.g
     * @param
     * @return top.jpower.jpower.module.common.jackson.Long2StringJacksonConverter
     **/
    @Bean
    @ConditionalOnMissingBean(Long2StringJacksonConverter.class)
    public Long2StringJacksonConverter long2StringJacksonConverter() {
        return new Long2StringJacksonConverter();
    }

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperFactoryBean.class)
    public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean(@Autowired DateJacksonConverter dateJacksonConverter,@Autowired Long2StringJacksonConverter long2StringJacksonConverter) {
        Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();

        jackson2ObjectMapperFactoryBean.setSerializersByType(ChainMap.<Class<?>, JsonSerializer<?>>create()
                .put(Long.TYPE ,long2StringJacksonConverter)
                .put(Long.class ,long2StringJacksonConverter)
                .build());
        jackson2ObjectMapperFactoryBean.setDeserializers(dateJacksonConverter);
        return jackson2ObjectMapperFactoryBean;
    }


    @Bean
    @ConditionalOnMissingBean(MappingJackson2HttpMessageConverter.class)
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Autowired ObjectMapper objectMapper,@Autowired DateJacksonConverter dateJacksonConverter) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        // java8日期日期处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        objectMapper.registerModule(javaTimeModule);

        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
