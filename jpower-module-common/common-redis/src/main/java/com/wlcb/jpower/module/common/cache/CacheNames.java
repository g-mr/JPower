package com.wlcb.jpower.module.common.cache;

/**
 * @ClassName CacheNames
 * @Description TODO 缓存名
 * @Author 郭丁志
 * @Date 2020-07-27 22:54
 * @Version 1.0
 */
public interface CacheNames {


    String DICT_REDIS_TYPE_MAP_KEY = "dict:typeMap:";
    String DICT_REDIS_CODE_KEY = "dict:code:";

    /** 行政区域缓存 **/
    String CITY_REDIS_CACHE = "jpower:citys";
    /** 系统参数缓存 **/
    String PARAMS_REDIS_CACHE = "jpower:params";
    /** 字典缓存 **/
    String DICT_REDIS_CACHE = "jpower:dicts";
    /** 系统缓存 **/
    String SYSTEM_REDIS_CACHE = "jpower:system";
    /** 系统缓存 **/
    String USER_REDIS_CACHE = "jpower:user";


    /** 登陆验证码缓存KEY **/
    String CAPTCHA_KEY = "jpower:auth::captcha:";
    /** 手机号验证码缓存KEY **/
    String PHONE_KEY = "jpower:auth::phone:";
    /** 登陆用户权限缓存KEY **/
    String TOKEN_URL_KEY = "jpower:auth::token:";

    /** 行政树形区域 **/
    String CITY_PARENT_REDIS_KEY = CITY_REDIS_CACHE+"::list:pcodeTree";
    /** 行政列表区域 **/
    String CITY_PARENT_LIST_REDIS_KEY = CITY_REDIS_CACHE+"::list:select";
    /** 行政Code区域 **/
    String CITY_PARENT_CODE_REDIS_KEY = CITY_REDIS_CACHE+"::list:parentCode";

    /** 系统参数key（code） **/
    String PARAMS_REDIS_CODE_KEY = "param:code:";
    /** 部门key **/
    String SYSTEM_ORG_ID_KEY = "org:id:";
    /** 部门下级所有ID key **/
    String SYSTEM_ORG_PARENT_KEY = "org:parent:";
    /** 客户端信息 key **/
    String SYSTEM_CLIENT_KEY = "client:code:";
    /** 角色的权限URL key **/
    String SYSTEM_URL_ROLES_KEY = "url:roles:";
    /** 用户信息 key（手机号） **/
    String USER_PHPNE_KEY = "user:phone:";
    /** 用户信息 key（loginId） **/
    String USER_LOGINID_KEY = "user:loginId:";
    /** 用户角色列表Id key（userid） **/
    String USER_ROLEID_USERID_KEY = "user:roleid:";
    /** 用户信息Id key（otherCode） **/
    String USER_OTHERCODE_KEY = "user:otherCode:";

}