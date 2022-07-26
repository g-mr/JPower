package com.wlcb.jpower.module.common.utils.constants;

/**
 * 返回值常量
 *
 * @author mr.g
 **/
public class ConstantsReturn {
    
    /**
     * 参数为空返回值
     **/
    public static final Integer RECODE_NULL = 406;
    /**
     * 成功返回值
     **/
    public static final Integer RECODE_SUCCESS = 200;
    /**
     * 找不到返回值
     **/
    public static final Integer RECODE_NOTFOUND = 404;
    /**
     * 业务逻辑返回值
     **/
    public static final Integer RECODE_BUSINESS = 300;
    /**
     * 系统异常
     **/
    public static final Integer RECODE_SYSTEM = 500;
    /**
     * 报错
     **/
    public static final Integer RECODE_ERROR = 501;
    /**
     * 操作失败
     **/
    public static final Integer RECODE_FAIL = 505;
    /**
     * API报错
     **/
    public static final Integer RECODE_API = 600;
    /**
     * 流量限制
     **/
    public static final Integer RECODE_RATELIMIT = 520;
    /**
     * Redis异常
     **/
    public static final Integer RECODE_REDIS = 550;
    /**
     * HTTP异常
     **/
    public static final Integer RECODE_HTTP = 540;
    /**
     * 解析异常
     **/
    public static final Integer RECODE_PARSER = 510;
    /**
     * 权限异常
     **/
    public static final Integer RECODE_AUTH = 401;

}
