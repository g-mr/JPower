package com.wlcb.jpower.module.tenant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JpowerTenantProperties
 * @Description TODO 多租户配置
 * @Author 郭丁志
 * @Date 2020-10-14 21:12
 * @Version 1.0
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
