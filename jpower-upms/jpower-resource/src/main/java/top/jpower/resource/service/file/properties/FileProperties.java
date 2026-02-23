package top.jpower.resource.service.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 文件配置类
 * <p>
 * 包含文件上传的各种配置选项
 * </p>
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties("jpower.file")
public class FileProperties implements Serializable {

    private static final long serialVersionUID = -6380298514488155175L;

    /**
     * 服务器配置
     *
     * @author mr.g
     */
    private Server server = new Server();

    /**
     * FastDFS配置
     *
     * @author mr.g
     */
    private FastDfs fastDfs = new FastDfs();

    @Data
    public final static class Server implements Serializable {

        private static final long serialVersionUID = 3018113038684389280L;

        /**
         * 外链域名
         *
         * @author mr.g
         */
        private String domain;
        /**
         * 上传文件保存路径
         *
         * @author mr.g
         */
        private String path;
    }

    @Data
    public final static class FastDfs implements Serializable{

        private static final long serialVersionUID = 2927648883910932669L;

        /**
         * 外链域名
         *
         * @author mr.g
         */
        private String domain;

    }
}
