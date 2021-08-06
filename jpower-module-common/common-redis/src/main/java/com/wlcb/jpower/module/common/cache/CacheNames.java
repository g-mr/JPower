package com.wlcb.jpower.module.common.cache;

/**
 * @ClassName CacheNames
 * @Description TODO 缓存名
 * @Author 郭丁志
 * @Date 2020-07-27 22:54
 * @Version 1.0
 */
public interface CacheNames {

    /** 登陆验证码缓存KEY **/
    String CAPTCHA_KEY = "jpower:auth::captcha:";
    /** 手机号验证码缓存KEY **/
    String PHONE_KEY = "jpower:auth::phone:";
    /** 登陆用户权限缓存KEY **/
    String TOKEN_URL_KEY = "jpower:auth::token:";
    /** 登录用户数据权限缓存KEY **/
    String TOKEN_DATA_SCOPE_KEY = "jpower:auth::dataScope:";

    /** 行政区域缓存 **/
    String CITY_REDIS_CACHE = "jpower:citys";
    /** 系统参数缓存 **/
    String PARAMS_REDIS_CACHE = "jpower:params";
    /** 字典缓存 **/
    String DICT_REDIS_CACHE = "jpower:dicts";
    /** 系统缓存 **/
    String SYSTEM_REDIS_CACHE = "jpower:system";
    /** 用户缓存 **/
    String USER_REDIS_CACHE = "jpower:user";
    /** 用户缓存 **/
    String FILE_REDIS_CACHE = "jpower:file";
    /** 数据权限缓存 **/
    String DATASCOPE_REDIS_CACHE = "jpower:dataScope";


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
    /** 角色的菜单 key **/
    String SYSTEM_MENU_ROLES_KEY = "menu:roles:";
    /** 所有角色的数据权限 key **/
    String SYSTEM_DATASCOPE_ALLROLES_KEY = "dataScope:roles:all:";
    /** 角色的数据权限 key **/
    String SYSTEM_DATASCOPE_ROLES_KEY = "dataScope:roles:";
    /** 角色名称 key **/
    String SYSTEM_ROLES_NAME_KEY = "role:name:";
    /** 地区信息 key **/
    String SYSTEM_CITY_CODE_KEY = "city:code:";

    /** 租户信息 key（tenantCode） **/
    String SYSTEM_TENANT_CODE_KEY = "tenant:code:";
    /** 用户信息 key（手机号） **/
    String USER_PHPNE_KEY = "user:phone:";
    /** 用户信息 key（loginId） **/
    String USER_LOGINID_KEY = "user:loginId:";
    /** 用户角色列表Id key（userid） **/
    String USER_ROLEID_USERID_KEY = "user:roleid:";
    /** 用户信息Id key（otherCode） **/
    String USER_OTHERCODE_KEY = "user:otherCode:";
    /** 字典列表 key(dictType) **/
    String DICT_TYPE_KEY = "dict:type:";

    /** 文件详情 key(base) **/
    String FILE_BASE_KEY = "base:detail:";

}