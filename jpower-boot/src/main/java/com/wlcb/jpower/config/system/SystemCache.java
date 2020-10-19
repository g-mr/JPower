package com.wlcb.jpower.config.system;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.service.client.CoreClientService;
import com.wlcb.jpower.service.client.impl.CoreClientServiceImpl;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.service.org.impl.CoreOrgServiceImpl;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.service.role.impl.CoreFunctionServiceImpl;

import java.util.List;

/**
 * @ClassName ParamConfig
 * @Description TODO 获取配置文件参数
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
public class SystemCache {

    private static CoreOrgService coreOrgService;
    private static CoreClientService coreClientService;
    private static CoreFunctionService coreFunctionService;

    static {
        coreOrgService = SpringUtil.getBean(CoreOrgServiceImpl.class);
        coreClientService = SpringUtil.getBean(CoreClientServiceImpl.class);
        coreFunctionService = SpringUtil.getBean(CoreFunctionServiceImpl.class);
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
            TbCoreOrg org = coreOrgService.getById(orgId);
            return org;
        });
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据部门ID获取下级所有ID
     * @Date 15:47 2020-05-06
     **/
    public static List<String> getChildIdOrgById(String orgId) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ORG_PARENT_KEY,orgId,() -> {
            List<String> responseData = coreOrgService.queryChildById(orgId);
            return responseData;
        });
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取客户端信息
     * @Date 15:47 2020-05-06
     **/
    public static TbCoreClient getClientByClientCode(String clientCode) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_CLIENT_KEY,clientCode,() -> {
            TbCoreClient responseData = coreClientService.loadClientByClientCode(clientCode);
            return responseData;
        });
    }

    /**
     * @author 郭丁志
     * @Description //TODO
     * @date 23:51 2020/10/17 0017
     * @param roleIds 角色ID
     */
    public static List<Object> getUrlsByRoleIds(List<String> roleIds) {
        String roles = Fc.join(roleIds);
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_URL_ROLES_KEY,roles,() -> {
            List<Object> responseData = coreFunctionService.getUrlsByRoleIds(roles);
            return responseData;
        });
    }
}
