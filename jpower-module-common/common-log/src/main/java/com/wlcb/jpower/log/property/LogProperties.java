package com.wlcb.jpower.log.property;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author mr.g
 * @Date 2022/1/17 0017 21:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "jpower.log")
public class LogProperties {

    /**
     * 日志输出方式
     */
    //Collections.singletonList(LogGenre.file);
    private List<LogGenre> mode = ListUtil.of(LogGenre.file,LogGenre.skywalking);

    /**
     * ELK配置
     * @author mr.g
     * @param null
     * @return
     */
    private ELK elk = new ELK();
    /**
     * 文件配置
     * @author mr.g
     * @param null
     * @return
     */
    private FILE file = new FILE();

    /**
     * 文件配置
     * @author mr.g
     * @param null
     * @return
     */
    private SKYWALKING skywalking = new SKYWALKING();

    @Data
    public static class SKYWALKING {
        /**
         * 日志输出格式
         */
        private String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%tid] [%thread] [%logger] - %msg%n";
    }

    @Data
    public static class FILE {
        /**
         * 文件路径
         */
        private String base = "./logs";

        /**
         * 日志保留天数
         */
        private int historyDay = 30;
        /**
         * 日志输出格式
         */
        private String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%tid] [%thread] [%logger] - %msg%n";
    }

    @Data
    public static class ELK {
        /**
         * elk logstash地址 eg:127.0.0.1:9001
         */
        private String destination;
    }

    public enum LogGenre {
        /**
         * 文件
         */
        file,
        /**
         * skywalking
         */
        skywalking,
        /**
         * elk
         */
        elk
    }
}
