package com.wlcb.jpower.module.common.cache;

/**
 * @ClassName CacheNames
 * @Description TODO 缓存名
 * @Author 郭丁志
 * @Date 2020-07-27 22:54
 * @Version 1.0
 */
public interface CacheNames {


    String DICT_VALUE = "dict:value";
    String DICT_LIST = "dict:list";

    /** 登陆验证码缓存KEY **/
    String CAPTCHA_KEY = "jpower:auth::captcha:";
    /** 手机号验证码缓存KEY **/
    String PHONE_KEY = "jpower:auth::phone:";
    /** 登陆用户权限缓存KEY **/
    String TOKEN_URL_KEY = "jpower:auth::token:";

    /** 行政树形区域 **/
    String CITY_PARENT_REDIS_KEY = "jpower:city::list:pcodeTree";
    /** 行政列表区域 **/
    String CITY_PARENT_LIST_REDIS_KEY = "jpower:city::list:select";
    /** 行政Code区域 **/
    String CITY_PARENT_CODE_REDIS_KEY = "jpower:city::list:parentCode";

    /** 系统参数key（code） **/
    String PARAMS_REDIS_CODE_KEY = "param:code:";


    /** 系统参数缓存 **/
    String PARAMS_REDIS_CACHE = "jpower:params";
}