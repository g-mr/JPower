package com.wlcb.jpower.module.common.cache;

/**
 * @ClassName CacheNames
 * @Description TODO 缓存名
 * @Author 郭丁志
 * @Date 2020-07-27 22:54
 * @Version 1.0
 */
public interface CacheNames {

    /**
     * 鉴权缓存
     **/

    String AUTH_KEY = "jpower:auth:";
    /* 登陆验证码缓存KEY **/
    String CAPTCHA_KEY = AUTH_KEY+"captcha:";
    /* 手机号验证码缓存KEY **/
    String PHONE_KEY = AUTH_KEY+"phone:";
    /* 登陆用户权限缓存KEY **/
    String TOKEN_URL_KEY = AUTH_KEY+"token:";
    /* 登录用户数据权限缓存KEY **/
    String TOKEN_DATA_SCOPE_KEY = AUTH_KEY+"dataScope:";


    /**
     * 用户缓存
     **/

    String USER_KEY = "jpower:user";
    /* 通过手机号查询用户 **/
    String USER_PHPNE_KEY = "phone:";
    /* 通过账号查询用户 **/
    String USER_LOGINID_KEY = "loginId:";
    /* 获取用户的所有角色ID **/
    String USER_ROLEID_KEY = "roleId:";
    /* 通过第三方CODE获取用户 **/
    String USER_OTHERCODE_KEY = "otherCode:";
    /* 通过ID获取用户信息 **/
    String USER_DETAIL_KEY = "detail:";

    /**
     * 系统参数缓存
     **/

    String PARAM_KEY = "jpower:param";
    /* 系统参数key（code） **/
    String PARAM_CODE_KEY = "code:";

    /**
     * 字典缓存
     **/

    String DICT_KEY = "jpower:dict";
    /* 通过字典类型查询字典列表 **/
    String DICT_TYPE_KEY = "type:";

    /**
     * 行政区域缓存
     **/

    String CITY_KEY = "jpower:city";

    /* 通过CODE获取地区 **/
    String CITY_CODE_KEY = "code:";

    /**
     * 数据权限缓存
     **/

    String DATASCOPE_KEY = "jpower:dataScope";
    /* 查询可所有角色执行得数据权限 **/
    String DATASCOPE_ALLROLE_KEY = "allRole:";
    /* 根据角色ID获取数据权限 **/
    String DATASCOPE_CLIENT_ROLE_KEY = "dataScope:client:role:";

    /**
     * 功能菜单缓存
     **/

    String FUNCTION_KEY = "jpower:function";
    /* 通过角色ID查询接口URL **/
    String URL_CLIENT_ROLE_KEY = "url:client:role:";
    /* 通过角色ID查询所有菜单 **/
    String MENU_CLIENT_ROLE_KEY = "menu:client:role:";

    /**
     * 组织架构缓存
     **/

    String ORG_KEY = "jpower:org";

    /* 通过ID获取部门 **/
    String ORG_DETAIL_KEY = "detail:";
    /* 根据部门ID获取下级所有ID key **/
    String ORG_CHILDID_KEY = "childId:";

    /**
     * 文件缓存
     **/

    String FILE_KEY = "jpower:file";
    /** 文件详情 **/
    String FILE_BASE_KEY = "detail:";

    /**
     * 租户缓存
     **/

    String TENANT_KEY = "jpower:tenant";
    /* 通过CODE获取租户信息 **/
    String TENANT_CODE_KEY = "code:";

    /**
     * 客户端缓存
     **/

    String CLIENT_KEY = "jpower:client";
    /* 通过CODE获取客户端信息 **/
    String CLIENTCODE_KEY = "code:";

    /**
     * 角色缓存
     **/

    String ROLE_KEY = "jpower:role";
    /* 通过角色ID获取角色名称 **/
    String ROLENAME_KEY = "name:";


    /** 行政树形区域 **/
    String CITY_PARENT_REDIS_KEY = "city:list:pcodeTree";
    /** 行政列表区域 **/
    String CITY_PARENT_LIST_REDIS_KEY = "city:list:select";
    /** 行政Code区域 **/
    String CITY_PARENT_CODE_REDIS_KEY = "city:list:parentCode";
}