package top.jpower.core.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权配置
 *
 * @author mr.g
 **/
@Data
@ConfigurationProperties("jpower.auth")
public class AuthProperties {

    /**
     * 放行API集合
     */
    private final List<String> skipUrl = new ArrayList<>();
    /**
     * 白名单集合
     */
    private List<String> whileIp = new ArrayList<>();
    /**
     * 客户端信息
     */
    private List<Client> client = new ArrayList<>();

    /**
     * 是否使用cookie
     * <br> 开启cookie之后不同的子域名下会共享token，如果是不同的子域名对应不同的租户，就会造成token混乱，这种情况不能打开cookie。只有不同的租户对应不同的主体域名情况下才开打开cookie </br>
     */
    private Boolean cookie = Boolean.FALSE;

    @Data
    public static class Client{
        /** 客户端编码 */
        private String code;
        /** 接口地址规则 */
        private List<String> path = new ArrayList<>();
    }
}
