package com.wlcb.jpower.module.common.utils.constants;


import java.util.Base64;

/**
 * Token配置常量
 *
 * @author mr.g
 **/
public interface TokenConstant {
    /** token请求头 **/
    String HEADER = "jpower-auth";
    /** 租户编码请求头 **/
    String HEADER_TENANT = "Tenant-Code";
    /** 菜单编码请求头 **/
    String HEADER_MENU = "Menu-Code";
    /** token前缀类型 **/
    String TOKEN_PREFIX = "jpower";

    /** JWT TOKEN 类型 **/
    String TOKEN_TYPE = "token_type";
    /** JWT TOKEN类型值 **/
    String ACCESS_TOKEN = "access_token";
    /** JWT TOKEN类型值 **/
    String REFRESH_TOKEN = "refresh_token";

    /** 刷新token的参数名称 **/
    String USER_ID = "userId";
    /** token 客户端参数名称 **/
    String CLIENT_CODE = "clientCode";
    /** 租户参数名称 **/
    String TENANT_CODE = "tenantCode";
    /** 匿名用户和白名单的HEADER **/
    String PASS_HEADER_NAME = "jpower-auth-pass";
    /** 数据权限的HEADER **/
    String DATA_SCOPE_NAME = "data-scope";

    /**
     * JWT密钥
     **/

    String SIGN_KEY = "Jpower";
    byte[] JWT_BASE64_SECURITY = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(SIGN_KEY.getBytes(CharsetKit.CHARSET_UTF_8)));
}
