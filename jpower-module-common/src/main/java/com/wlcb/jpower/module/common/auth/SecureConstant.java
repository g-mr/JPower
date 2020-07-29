package com.wlcb.jpower.module.common.auth;

/**
 * @ClassName SecureConstant
 * @Description TODO 授权校验常量
 * @Author 郭丁志
 * @Date 2020-07-27 16:13
 * @Version 1.0
 */
public interface SecureConstant {

    /**
     * 认证请求头
     */
    String BASIC_HEADER_KEY = "Authorization";

    /**
     * 认证请求头前缀
     */
    String BASIC_HEADER_PREFIX = "Basic ";

    /**
     * 认证请求头前缀
     */
    String BASIC_HEADER_PREFIX_EXT = "Basic%20";


}
