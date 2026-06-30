package com.qidiangk.smart.system.controller.api;

import com.qidiangk.smart.system.api.dto.*;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.api.feign.SystemClient;
import com.qidiangk.smart.system.dbs.entity.client.CoreClient;
import com.qidiangk.smart.system.dbs.entity.tenant.CoreTenant;
import com.qidiangk.smart.system.service.city.CoreCityService;
import com.qidiangk.smart.system.service.client.CoreClientService;
import com.qidiangk.smart.system.service.org.CoreOrgService;
import com.qidiangk.smart.system.service.role.CoreDataScopeService;
import com.qidiangk.smart.system.service.role.CoreFunctionService;
import com.qidiangk.smart.system.service.role.CoreRoleService;
import com.qidiangk.smart.system.service.tenant.TenantService;

import java.util.List;

/**
 * 
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("/feign/core")
@RequiredArgsConstructor
public class SystemClientController extends BaseController implements SystemClient {

    private final CoreOrgService coreOrgService;
    private final CoreClientService coreClientService;
    private final CoreFunctionService coreFunctionService;
    private final TenantService tenantService;
    private final CoreDataScopeService coreDataScopeService;
    private final CoreRoleService coreRoleService;
    private final CoreCityService coreCityService;

    @Override
    @GetMapping("/org/queryChildById")
    public R<List<Long>> queryChildOrgById(@RequestParam Long id){
        return R.data(coreOrgService.queryChildIdById(id));
    }

	@Override
	@GetMapping("/org/queryOrgById")
	public R<OrgDTO> queryOrgById(@RequestParam Long orgId) {
		return R.data(coreOrgService.getByIdAs(orgId, OrgDTO.class));
	}

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public R<List<String>> getUrlsByRoleIds(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return R.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds,clientCode));
    }

    @Override
    @GetMapping("/function/getMenuListByRole")
    public R<List<FunctionDTO>> getMenuListByRole(@RequestParam List<Long> roleIds, @RequestParam String clientCode, @RequestParam(required = false) Long topMenuId) {
        return R.data(BeanUtil.copyToList(coreFunctionService.listMenuByRoleId(roleIds, clientCode, topMenuId), FunctionDTO.class));
    }

    @Override
    @GetMapping("/dataScope/getDataScopeByRole")
    public R<List<DataScopeDTO>> getDataScopeByRole(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return R.data(coreDataScopeService.getDataScopeByRole(roleIds, clientCode));
    }

    @Override
    @GetMapping("/role/getRoleNameByIds")
    public R<List<String>> getRoleNameByIds(@RequestParam List<Long> roleIds) {
        return R.data(coreRoleService.getRoleNameByIds(roleIds));
    }

    @Override
    @GetMapping("/tenant/getTenantByCode")
    public R<TenantDTO> getTenantByCode(@RequestParam String tenantCode){
        return R.data(BeanUtil.copyProperties(tenantService.getOneByField(CoreTenant::getTenantCode, tenantCode), TenantDTO.class));
    }

    @Override
    @GetMapping("/client/getClientByClientCode")
    public R<ClientDTO> getClientByClientCode(@RequestParam String clientCode) {
        return R.data(BeanUtil.copyProperties(coreClientService.getOneByField(CoreClient::getClientCode, clientCode), ClientDTO.class));
    }

    @Override
    @GetMapping("/city/getCityByCode")
    public R<CityDTO> getCityByCode(@RequestParam String code) {
        return R.data(BeanUtil.copyProperties(coreCityService.queryByCode(code), CityDTO.class));
    }

    @GetMapping("/menu/getIdByCode")
    public R<Long> getMenuIdByCode(@RequestParam String code) {
        return R.data(coreFunctionService.getIdByCode(code));
    }
}
