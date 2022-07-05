package com.wlcb.jpower.module.common.utils.constants;


import java.util.Base64;

/**
 * @ClassName TokenConstant
 * @Description TODO Token配置常量
 * @Author 郭丁志
 * @Date 2020-07-27 15:53
 * @Version 1.0
 */
public interface TokenConstant {
    String SIGN_KEY = "Jpower";
    String HEADER = "jpower-auth";
    String HEADER_TENANT = "Tenant-Code";
    String HEADER_MENU = "Menu-Code";
    String JPOWER = "jpower";
    String TOKEN_TYPE = "token_type";
    String ACCESS_TOKEN = "access_token";
    String REFRESH_TOKEN = "refresh_token";
    String USER_ID = "userId";
    String TENANT_CODE = "tenantCode";
    String CLIENT_CODE = "clientCode";
    /** 匿名用户和白名单的HEADER **/
    String PASS_HEADER_NAME = "jpower-auth-pass";
    /** 数据权限的HEADER **/
    String DATA_SCOPE_NAME = "data-scope";

    /**
     * JWT密钥
     **/
    byte[] JWT_BASE64_SECURITY = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(SIGN_KEY.getBytes(CharsetKit.CHARSET_UTF_8)));
}
