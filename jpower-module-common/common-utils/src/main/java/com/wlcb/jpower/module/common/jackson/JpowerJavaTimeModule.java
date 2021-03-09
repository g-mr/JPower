package com.wlcb.jpower.module.common.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.wlcb.jpower.module.common.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName JpowerJavaTimeModule
 * @Description TODO java 8 时间默认序列化
 * @Author 郭丁志
 * @Date 2021/3/10 0010 3:27
 * @Version 1.0
 */
public class JpowerJavaTimeModule extends SimpleModule {

    public JpowerJavaTimeModule() {
        super(PackageVersion.VERSION);
        this.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME)));
        this.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE)));
        this.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_TIME)));
        this.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME)));
        this.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE)));
        this.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.PATTERN_TIME)));
    }

}
