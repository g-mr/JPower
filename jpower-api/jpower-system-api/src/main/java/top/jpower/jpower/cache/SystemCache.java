package top.jpower.jpower.cache;

import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.jpower.dto.*;
import top.jpower.jpower.feign.SystemClient;

import java.util.List;

/**
 * 系统缓存
 *
 * @author mr.g
 **/
public class SystemCache {

    private static final SystemClient SYSTEM_CLIENT;

    static {
        SYSTEM_CLIENT = SpringUtil.getBean(SystemClient.class);
    }

    /**
     * 获取部门名称
     *
     * @author mr.g
     * @param orgId 组织机构ID
     * @return 组织机构名称
     **/
    public static String getOrgName(Long orgId) {
        OrgDTO org = getOrg(orgId);
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
    public static OrgDTO getOrg(Long orgId){
        return CacheUtil.get(CacheNames.ORG_KEY,CacheNames.ORG_DETAIL_KEY,orgId,() -> {
            R<OrgDTO> r = SYSTEM_CLIENT.queryOrgById(orgId);
            return r.getData();
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
            R<List<Long>> r = SYSTEM_CLIENT.queryChildOrgById(orgId);
            return r.getData();
        });
    }
    
    /**
     * 获取地区名称
     *
     * @author mr.g
     * @param code 地区CODE
     * @return java.lang.String
     **/
    public static String getCityName(String code) {
        CityDTO city = getCity(code);
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
    public static CityDTO getCity(String code) {
        return CacheUtil.get(CacheNames.CITY_KEY,CacheNames.CITY_CODE_KEY,code,() -> {
            R<CityDTO> r = SYSTEM_CLIENT.getCityByCode(code);
            return r.getData();
        });
    }

    /**
     * 通过CODE获取客户端信息
     *
     * @author mr.g
     * @param clientCode 客户端CODE
     * @return 客户端详情
     **/
    public static ClientDTO getClientByClientCode(String clientCode) {
        return CacheUtil.get(CacheNames.CLIENT_KEY,CacheNames.CLIENTCODE_KEY,clientCode,() -> {
            R<ClientDTO> r = SYSTEM_CLIENT.getClientByClientCode(clientCode);
            return r.getData();
        });
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
            R<List<String>> r = SYSTEM_CLIENT.getUrlsByRoleIds(roleIds,clientCode);
            return r.getData();
        });
    }

    /**
     * 通过角色ID获取指定客户端的所有菜单
     *
     * @author mr.g
     * @date 23:28 2020/11/5 0005
     * @param roleIds 角色ID
     */
    public static List<FunctionDTO> getMenuListByRole(List<Long> roleIds, String clientCode) {
        return CacheUtil.get(CacheNames.FUNCTION_KEY,CacheNames.MENU_CLIENT_ROLE_KEY,clientCode+StringPool.COLON+roleIds,() -> {
            R<List<FunctionDTO>> r = SYSTEM_CLIENT.getMenuListByRole(roleIds, clientCode, null);
            return r.getData();
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
    public static List<DataScopeDTO> getDataScopeByRole(List<Long> roleIds, String clientCode) {
        return CacheUtil.get(CacheNames.DATASCOPE_KEY,CacheNames.DATASCOPE_CLIENT_ROLE_KEY,clientCode+StringPool.COLON+roleIds,() -> {
            R<List<DataScopeDTO>> r = SYSTEM_CLIENT.getDataScopeByRole(roleIds,clientCode);
            return r.getData();
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
            R<List<String>> r = SYSTEM_CLIENT.getRoleNameByIds(roleIds);
            return r.getData();
        });
    }

    /**
     * 通过CODE获取租户信息
     *
     * @author mr.g
     * @param tenantCode 租户CODE
     * @return 租户信息
     **/
    public static TenantDTO getTenantByCode(String tenantCode) {
        return CacheUtil.get(CacheNames.TENANT_KEY,CacheNames.TENANT_CODE_KEY,tenantCode,() -> {
            R<TenantDTO> r = SYSTEM_CLIENT.getTenantByCode(tenantCode);
            return r.getData();
        });
    }

}