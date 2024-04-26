package top.jpower.jpower.operate.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 文件配置
 *
 * @author mr.g
 * @date 2024/4/24 10:54 AM
 */
@Data
@ConfigurationProperties("jpower.file")
public class FileProperties implements Serializable {

    private static final long serialVersionUID = -6380298514488155175L;

    /**
     * 服务器配置
     **/
    private Server server = new Server();

    /**
     * 服务器配置
     **/
    private FastDfs fastDfs = new FastDfs();

    @Data
    public final static class Server implements Serializable {

        private static final long serialVersionUID = 3018113038684389280L;

        /**
         * 外链域名
         **/
        private String domain;
        /**
         * 上传文件保存路径
         **/
        private String path;
    }

    @Data
    public final static class FastDfs implements Serializable{

        private static final long serialVersionUID = 2927648883910932669L;

        /**
         * 外链域名
         **/
        private String domain;

    }
}
