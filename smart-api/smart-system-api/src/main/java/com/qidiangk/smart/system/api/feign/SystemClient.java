package com.qidiangk.smart.system.api.feign;

import com.qidiangk.smart.system.api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;

import java.util.List;

/**
 * 系统服务客户端
 *
 * @author mr.g
 */
@FeignClient(value = AppConstant.JPOWER_SYSTEM, fallback = SystemClientFallback.class, path = "/feign/core/")
public interface SystemClient {

    /**
     * 查询部门所有下级部门id
     *
     * @author mr.g
     */
    @GetMapping("/org/queryChildById")
    R<List<Long>> queryChildOrgById(@RequestParam("id") Long id);

    @GetMapping("/org/queryOrgById")
    R<OrgDTO> queryOrgById(@RequestParam("orgId") Long orgId);

    @GetMapping("/client/getClientByClientCode")
    R<ClientDTO> getClientByClientCode(@RequestParam("clientCode") String clientCode);

    @GetMapping("/function/getUrlsByRoleIds")
    R<List<String>> getUrlsByRoleIds(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode);

    @GetMapping("/tenant/getTenantByCode")
    R<TenantDTO> getTenantByCode(@RequestParam("tenantCode") String tenantCode);

    @GetMapping("/function/getMenuListByRole")
    R<List<FunctionDTO>> getMenuListByRole(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode, @RequestParam("topMenuId") Long topMenuId);

    @GetMapping("/dataScope/getDataScopeByRole")
    R<List<DataScopeDTO>> getDataScopeByRole(@RequestParam("roleIds") List<Long> roleIds, @RequestParam("clientCode") String clientCode);

    @GetMapping("/role/getRoleNameByIds")
    R<List<String>> getRoleNameByIds(@RequestParam("roleIds") List<Long> roleIds);

    @GetMapping("/city/getCityByCode")
    R<CityDTO> getCityByCode(@RequestParam("code") String code);
}
