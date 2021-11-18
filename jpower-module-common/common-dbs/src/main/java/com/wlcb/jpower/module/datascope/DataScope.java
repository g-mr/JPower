package com.wlcb.jpower.module.datascope;

import lombok.Data;

import java.util.Set;

/**
 * @ClassName DataAuth
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/11/5 0005 1:10
 * @Version 1.0
 */
@Data
public class DataScope {

    private String scopeClass;

    private Integer scopeType;

    private String scopeColumn;

    private String scopeField;

    /** 自定义值域 **/
    private String scopeValue;

    private Set<String> ids;

}
