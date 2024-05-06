package top.jpower.jpower.cache;

import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.feign.SystemClient;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;

import java.util.List;

/**
 * 系统缓存
 *
 * @author mr.g
 **/
public class SystemCache {

    private static SystemClient systemClient;

    static {
        systemClient = SpringUtil.getBean(SystemClient.class);
    }

    /**
     * 获取部门名称
     *
     * @author mr.g
     * @param orgId 组织机构ID
     * @return 组织机构名称
     **/
    public static String getOrgName(Long orgId){
        TbCoreOrg org = getOrg(orgId);
        if (Fc.isNull(org)){
            return StringPool.EMPTY;
        }
        return org.getName();
    }

    /**
     * 通过ID获取部门
     * 
     * @param orgId 组织机构ID
     * @return 部门详情
     */
    public static TbCoreOrg getOrg(Long orgId){
        return CacheUtil.get(CacheNames.ORG_KEY,CacheNames.ORG_DETAIL_KEY,orgId,() -> {
            ResponseData<TbCoreOrg> responseData = systemClient.queryOrgById(orgId);
            return responseData.getData();
        });
    }

    /**
     * 根据部门ID获取下级所有ID
     * 
     * @param orgId 组织机构ID
     * @return 下级ID列表
     */
    public static List<Long> getChildIdOrgById(Long orgId) {
        return CacheUtil.get(CacheNames.ORG_KEY,CacheNames.ORG_CHILDID_KEY,orgId,() -> {
            ResponseData<List<Long>> responseData = systemClient.queryChildOrgById(orgId);
            return responseData.getData();
        });
    }
    
    /**
     * 获取地区名称
     *
     * @author mr.g
     * @date 23:38 2021-02-21
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
     * 通过CODE获取地区
     *
     * @author mr.g
     * @param code 城市CODE
     * @return 城市详情
     **/
    public static TbCoreCity getCity(String code) {
        return CacheUtil.get(CacheNames.CITY_KEY,CacheNames.CITY_CODE_KEY,code,() -> {
            ResponseData<TbCoreCity> responseData = systemClient.getCityByCode(code);
            return responseData.getData();
        },Boolean.FALSE);
    }

    /**
     * 通过CODE获取客户端信息
     *
     * @author mr.g
     * @param clientCode 客户端CODE
     * @return 客户端详情
     **/
    public static TbCoreClient getClientByClientCode(String clientCode) {
        return CacheUtil.get(CacheNames.CLIENT_KEY,CacheNames.CLIENTCODE_KEY,clientCode,() -> {
            ResponseData<TbCoreClient> responseData = systemClient.getClientByClientCode(clientCode);
            return responseData.getData();
        },Boolean.FALSE);
    }

    /**
     * 通过角色ID查询指定客户端的接口URL
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @return URL列表
     **/
    public static List<String> getUrlsByRoleIds(List<Long> roleIds, String clientCode) {
        return CacheUtil.get(CacheNames.FUNCTION_KEY,CacheNames.URL_CLIENT_ROLE_KEY,clientCode+StringPool.COLON+roleIds,() -> {
            ResponseData<List<String>> responseData = systemClient.getUrlsByRoleIds(roleIds,clientCode);
            return responseData.getData();
        });
    }

    /**
     * 通过角色ID获取指定客户端的所有菜单
     *
     * @author mr.g
     * @date 23:28 2020/11/5 0005
     * @param roleIds 角色ID
     */
    public static List<TbCoreFunction> getMenuListByRole(List<Long> roleIds, String clientCode) {
        return CacheUtil.get(CacheNames.FUNCTION_KEY,CacheNames.MENU_CLIENT_ROLE_KEY,clientCode+StringPool.COLON+roleIds,() -> {
            ResponseData<List<TbCoreFunction>> responseData = systemClient.getMenuListByRole(roleIds, clientCode, null);
            return responseData.getData();
        });
    }

    /**
     * 查询可所有角色执行得数据权限
     *
     * @author mr.g
     * @date 23:31 2020/11/5 0005
     * @return 数据权限列表
     */
    public static List<TbCoreDataScope> getAllRoleDataScope() {
        return CacheUtil.get(CacheNames.DATASCOPE_KEY,CacheNames.DATASCOPE_ALLROLE_KEY,"all",() -> {
            ResponseData<List<TbCoreDataScope>> responseData = systemClient.getAllRoleDataScope();
            return responseData.getData();
        });
    }

    /**
     * 根据角色ID获取指定客户端的数据权限
     *
     * @author mr.g
     * @date 23:38 2020/11/5 0005
     * @param roleIds  角色ID
     * @return 数据权限列表
     */
    public static List<TbCoreDataScope> getDataScopeByRole(List<Long> roleIds,String clientCode) {
        return CacheUtil.get(CacheNames.DATASCOPE_KEY,CacheNames.DATASCOPE_CLIENT_ROLE_KEY,clientCode+StringPool.COLON+roleIds,() -> {
            ResponseData<List<TbCoreDataScope>> responseData = systemClient.getDataScopeByRole(roleIds,clientCode);
            return responseData.getData();
        });
    }

    /**
     * 通过角色ID获取角色名称
     *
     * @author mr.g
     * @date 23:38 2020/11/5 0005
     * @param roleIds  角色ID
     * @return 角色名称列表
     */
    public static List<String> getRoleNameByIds(List<Long> roleIds) {
        return CacheUtil.get(CacheNames.ROLE_KEY,CacheNames.ROLENAME_KEY,roleIds,() -> {
            ResponseData<List<String>> responseData = systemClient.getRoleNameByIds(roleIds);
            return responseData.getData();
        });
    }

    /**
     * 通过CODE获取租户信息
     *
     * @author mr.g
     * @param tenantCode 租户CODE
     * @return 租户信息
     **/
    public static TbCoreTenant getTenantByCode(String tenantCode) {
        return CacheUtil.get(CacheNames.TENANT_KEY,CacheNames.TENANT_CODE_KEY,tenantCode,() -> {
            ResponseData<TbCoreTenant> responseData = systemClient.getTenantByCode(tenantCode);
            return responseData.getData();
        }, Boolean.FALSE);
    }

}