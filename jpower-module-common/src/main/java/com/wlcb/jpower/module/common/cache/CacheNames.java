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

    String CAPTCHA_KEY = "jpower:auth::captcha:";
    String PHONE_KEY = "jpower:auth::phone:";
    String TOKEN_URL_KEY = "jpower:auth::token:";

    String PARAMS_REDIS_KEY = "jpower:params:";

}