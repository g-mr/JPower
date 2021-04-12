package com.wlcb.jpower.properties;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @Author mr.g
 * @Date 2021/4/11 0011 20:23
 */
@Data
public class AuthInfoConfiguration{

    /**
     * token接口
     */
    private String url = "";
    /**
     * 接口请求方式
     */
    private MethodType method = MethodType.GET;
    /**
     * 接口请求参数
     */
    private String params;
    /**
     * 接口请求header参数
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
     * token参数名称
     */
    private String tokenName = "Authorization";
    /**
     * token参数位置
     */
    private AuthTokenPosition tokenPosition = AuthTokenPosition.HEADER;
    /**
     * 过期时间 0代表永不过期
     */
    private long expiresIn = 0;
    /**
     * 接口返回得过期时间字段（优先级高于expiresIn）
     */
    private String expiresField;
    /**
     * 过期时间单位
     */
    private TimeUnit expiresUnit = TimeUnit.SECONDS;
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
    /**
     * 拼接前后缀字符的分隔符，默认为空格
     */
    private String tokenDelimiter = " ";

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