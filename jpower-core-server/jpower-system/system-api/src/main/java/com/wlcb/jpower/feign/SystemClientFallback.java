package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ParamsClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 15:31
 * @Version 1.0
 */
@Component
public class SystemClientFallback implements SystemClient {

    @Override
    public ResponseData<List<String>> queryChildOrgById(String id) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreOrg> queryOrgById(String orgId) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreClient> getClientByClientCode(String clientCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<Object>> getUrlsByRoleIds(String roleIds) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<TbCoreTenant> getTenantByCode(String tenantCode) {
        return ReturnJsonUtil.fail("查询失败");
    }
}
