package top.jpower.core.dbs.tenant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置类
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.tenant")
public class JpowerTenantProperties {

    /**
     * 多租户开关
     **/
    private Boolean enable = true;
    /**
     * 租户字段名称
     **/
    private String column = "tenant_code";
    /**
     * 排除租户管理的表
     **/
    private List<String> excludeTables = new ArrayList<>();
}
