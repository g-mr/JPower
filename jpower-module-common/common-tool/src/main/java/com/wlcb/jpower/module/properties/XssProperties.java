package com.wlcb.jpower.module.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ClientProperties
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:22
 * @Version 1.0
 */
@Data
@ConfigurationProperties("jpower.xss")
public class XssProperties {

    /** 是否开启富文本过滤 **/
    private Boolean isIncludeRichText = false;
    /** 不过滤得URL **/
    private List<String> excludes = new ArrayList<>();

    public List<String> getExcludes(){
        return excludes;
    }

    @Getter
    private static List<String> defaultExcludes = new ArrayList<>();

    static {
        defaultExcludes.addAll(AuthDefExculdesUrl.getExculudesUrl());
        defaultExcludes.removeIf(i -> i.startsWith("/auth"));
    }
}
