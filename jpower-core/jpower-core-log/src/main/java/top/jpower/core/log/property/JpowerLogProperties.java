package top.jpower.core.log.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置
 *
 * @author mr.g
 * @date 2022/1/17 0017 21:46
 */
@Data
@ConfigurationProperties(prefix = "jpower.log")
public class JpowerLogProperties {

    /**
     * 日志输出方式
     * <br/>
     * 多个逗号分割
     */
    private String mode;

    /**
     * ELK配置
     */
    private Elk elk = new Elk();
    /**
     * 文件配置
     */
    private File file = new File();

    /**
     * 文件配置
     */
    private Skywalking skywalking = new Skywalking();

    @Data
    public static class Skywalking {
        /**
         * 日志输出格式
         */
        private String pattern;
    }

    @Data
    public static class File {

        /**
         * INFO日志打印方式
         * <br/>
         * <p>warn:打印info、warn级别</p>
         * <p>info:打印info</p>
         * <p>threshold:打印info以下所有日志</p>
         **/
        private String info;
        /**
         * 文件路径
         */
        private String base;

        /**
         * 日志保留天数
         */
        private int historyDay;
        /**
         * 日志输出格式
         */
        private String pattern;
    }

    @Data
    public static class Elk {
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
