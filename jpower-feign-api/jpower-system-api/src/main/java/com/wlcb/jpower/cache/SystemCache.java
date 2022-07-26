package com.wlcb.jpower.cache;

import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.feign.SystemClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.List;

/**
 * @ClassName ParamConfig
 * @Description TODO 获取配置文件参数
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
public class SystemCache {

    private static SystemClient systemClient;

    static {
        systemClient = SpringUtil.getBean(SystemClient.class);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取部门名称
     * @Date 15:47 2020-05-06
     **/
    public static String getOrgName(String orgId){
        TbCoreOrg org = getOrg(orgId);
        if (Fc.isNull(org)){
            return StringPool.EMPTY;
        }
        return org.getName();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取部门
     * @Date 15:47 2020-05-06
     **/
    public static TbCoreOrg getOrg(String orgId){
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ORG_ID_KEY,orgId,() -> {
            ResponseData<TbCoreOrg> responseData = systemClient.queryOrgById(orgId);
            return responseData.getData();
        });
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据部门ID获取下级所有ID
     * @Date 15:47 2020-05-06
     **/
    public static List<String> getChildIdOrgById(String orgId) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ORG_PARENT_KEY,orgId,() -> {
            ResponseData<List<String>> responseData = systemClient.queryChildOrgById(orgId);
            return responseData.getData();
        });
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取客户端信息
     * @Date 15:47 2020-05-06
     **/
    public static TbCoreClient getClientByClientCode(String clientCode) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_CLIENT_KEY,clientCode,() -> {
            ResponseData<TbCoreClient> responseData = systemClient.getClientByClientCode(clientCode);
            return responseData.getData();
        },Boolean.FALSE);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 通过角色ID获取URL
     * @date 23:51 2020/10/17 0017
     * @param roleIds 角色ID
     */
    public static List<Object> getUrlsByRoleIds(List<String> roleIds) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_URL_ROLES_KEY,roleIds,() -> {
            ResponseData<List<Object>> responseData = systemClient.getUrlsByRoleIds(roleIds);
            return responseData.getData();
        });
    }

    /**
     * @author 郭丁志
     * @Description //TODO 获取租户信息
     * @date 17:38 2020/10/25 0025
     * @param tenantCode 租户编码
     * @return com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant
     */
    public static TbCoreTenant getTenantByCode(String tenantCode) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_TENANT_CODE_KEY,tenantCode,() -> {
            ResponseData<TbCoreTenant> responseData = systemClient.getTenantByCode(tenantCode);
            return responseData.getData();
        });
    }

    /**
     * 通过角色ID获取所有菜单
     *
     * @author 郭丁志
     * @date 23:28 2020/11/5 0005
     * @param roleIds 角色ID
     */
    public static List<TbCoreFunction> getMenuListByRole(List<String> roleIds) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_MENU_ROLES_KEY,roleIds,() -> {
            ResponseData<List<TbCoreFunction>> responseData = systemClient.getMenuListByRole(roleIds);
            return responseData.getData();
        });
    }

    /**
     * 查询可所有角色执行得数据权限
     *
     * @author 郭丁志
     * @date 23:31 2020/11/5 0005
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreDataScope>
     */
    public static List<TbCoreDataScope> getAllRoleDataScope() {
        return CacheUtil.get(CacheNames.DATASCOPE_REDIS_CACHE,CacheNames.SYSTEM_DATASCOPE_ALLROLES_KEY,"all",() -> {
            ResponseData<List<TbCoreDataScope>> responseData = systemClient.getAllRoleDataScope();
            return responseData.getData();
        });
    }

    /**
     * 根据角色ID获取所有指定角色拥有的数据权限
     *
     * @author 郭丁志
     * @date 23:38 2020/11/5 0005
     * @param roleIds  角色ID
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreDataScope>
     */
    public static List<TbCoreDataScope> getDataScopeByRole(List<String> roleIds) {
        return CacheUtil.get(CacheNames.DATASCOPE_REDIS_CACHE,CacheNames.SYSTEM_DATASCOPE_ROLES_KEY,roleIds,() -> {
            ResponseData<List<TbCoreDataScope>> responseData = systemClient.getDataScopeByRole(roleIds);
            return responseData.getData();
        });
    }

    /**
     * 通过角色ID获取角色名称
     *
     * @author 郭丁志
     * @date 23:38 2020/11/5 0005
     * @param roleIds  角色ID
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreDataScope>
     */
    public static List<String> getRoleNameByIds(List<String> roleIds) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ROLES_NAME_KEY,roleIds,() -> {
            ResponseData<List<String>> responseData = systemClient.getRoleNameByIds(roleIds);
            return responseData.getData();
        });
    }

    /**
     * 获取地区名称
     *
     * @Author ding
     * @Date 23:38 2021-02-21
     * @param code
     * @return java.lang.String
     **/
    public static String getCityName(String code) {
        TbCoreCity city = getCity(code);
        if (Fc.isNull(city)){
            return StringPool.EMPTY;
        }
        return city.getName();
    }

    /**
     * 获取地区
     * @Author ding
     * @Date 23:39 2021-02-21
     * @param code
     * @return com.wlcb.jpower.dbs.entity.city.TbCoreCity
     **/
    public static TbCoreCity getCity(String code) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_CITY_CODE_KEY,code,() -> {
            ResponseData<TbCoreCity> responseData = systemClient.getCityByCode(code);
            return responseData.getData();
        },Boolean.FALSE);
    }
}
