package com.wlcb.jpower.module.common.utils.constants;

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
    String JPOWER = "jpower";
    String TOKEN_TYPE = "token_type";
    String ACCESS_TOKEN = "access_token";
    String REFRESH_TOKEN = "refresh_token";
    String ACCOUNT = "loginId";
    String USER_ID = "userId";
    String USER_NAME = "userName";
    String TENANT_CODE = "tenantCode";
    String CLIENT_CODE = "clientCode";
    String TELE_PHONE = "telephone";
    String USER_TYPE = "userType";
    String ORG_ID = "orgId";
    String ROLE_IDS = "roleIds";
    String IS_SYS_USER = "isSysUser";
    Integer AUTH_LENGTH = 7;
    /** 匿名用户和白名单的HEADER **/
    String PASS_HEADER_NAME = "jpower-auth-pass";
}
