package com.wlcb.jpower.config.system;

import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
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

    public static TbCoreOrg getOrg(String orgId){
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ORG_ID_KEY,orgId,() -> {
            ResponseData<TbCoreOrg> responseData = systemClient.queryOrgById(orgId);
            return responseData.getData();
        });
    }

    public static List<String> getChildIdOrgById(String orgId) {
        return CacheUtil.get(CacheNames.SYSTEM_REDIS_CACHE,CacheNames.SYSTEM_ORG_PARENT_KEY,orgId,() -> {
            ResponseData<List<String>> responseData = systemClient.queryChildOrgById(orgId);
            return responseData.getData();
        });
    }
}
