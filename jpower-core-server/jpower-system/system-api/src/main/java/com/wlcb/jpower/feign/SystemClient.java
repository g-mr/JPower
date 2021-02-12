package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mr.gmac
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = SystemClientFallback.class, path = "/core/")
public interface SystemClient {

    /**
     * @author null
     * @Description //TODO 查询部门所有下级部门id
     * @date 0:42 2020/9/3 0003
     * @param id
     * @return com.wlcb.jpower.module.base.vo.ResponseData<java.lang.String>
     */
    @GetMapping("/org/queryChildById")
    ResponseData<List<String>> queryChildOrgById(@RequestParam String id);

    @GetMapping("/org/queryOrgById")
    ResponseData<TbCoreOrg> queryOrgById(@RequestParam String orgId);

    @GetMapping("/client/getClientByClientCode")
    ResponseData<TbCoreClient> getClientByClientCode(@RequestParam String clientCode);

    @GetMapping("/function/getUrlsByRoleIds")
    ResponseData<List<Object>> getUrlsByRoleIds(@RequestParam List<String> roleIds);

    @GetMapping("/tenant/getTenantByCode")
    ResponseData<TbCoreTenant> getTenantByCode(@RequestParam String tenantCode);

    @GetMapping("/function/getMenuListByRole")
    ResponseData<List<TbCoreFunction>> getMenuListByRole(@RequestParam List<String> roleIds);

    @GetMapping("/dataScope/getAllRoleDataScope")
    ResponseData<List<TbCoreDataScope>> getAllRoleDataScope();

    @GetMapping("/dataScope/getDataScopeByRole")
    ResponseData<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam List<String> roleIds);

    @GetMapping("/role/getRoleNameByIds")
    ResponseData<List<String>> getRoleNameByIds(@RequestParam List<String> roleIds);
}
