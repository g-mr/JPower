package com.wlcb.jpower.properties;

import lombok.Data;

/**
 * @Author mr.g
 * @Date 2021/4/11 0011 20:23
 */
@Data
public class AuthInfoConfiguration{

    /**
     * token接口
     */
    private String url;
    /**
     * 接口请求方式
     */
    private MethodType method;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 接口请求参数
     */
    private String params;
    /**
     * 接口请求得头
     */
    private String headers;
    /**
     * 标识是否成功得字段
     */
    private String codeField;
    /**
     * 成功标识
     */
    private String successCode = "200";
    /**
     * token （优先级高于tokenField）
     */
    private String token;
    /**
     * 接口返回得token字段
     */
    private String tokenField;
    /**
     * token参数位置
     */
    private AuthTokenPosition tokenPosition = AuthTokenPosition.HEADER;
    /**
     * 过期时间
     */
    private long expiresIn;
    /**
     * 接口返回得过期时间字段（优先级高于expiresIn）
     */
    private String expiresField;
    /**
     * 过期时间单位
     */
    private ExpiresTimeUnit expiresUnit;
    /**
     * token得前缀
     */
    private String tokenPrefix;
    /**
     * 接口返回得token得前缀字段（优先级高于tokenPrefix）
     */
    private String tokenPrefixField;
    /**
     * token得后缀
     */
    private String tokenSuffix;
    /**
     * 接口返回得token得后缀字段（优先级高于tokenSuffix）
     */
    private String tokenSuffixField;

    public enum AuthTokenPosition {

        /**
         * 请求头.
         */
        HEADER,

        /**
         * FORM.
         */
        FORM,

        /**
         * QUERY.
         */
        QUERY

    }

    public enum ExpiresTimeUnit {

        /**
         * 毫秒.
         */
        MS,

        /**
         * 秒.
         */
        SS,

        /**
         * 分钟.
         */
        MM,

        /**
         * 小时.
         */
        HH,

        /**
         * 天.
         */
        DD

    }

    public enum MethodType {

        /**
         * GET.
         */
        GET,

        /**
         * POST.
         */
        POST,

        /**
         * HEAD.
         */
        HEAD
    }
}