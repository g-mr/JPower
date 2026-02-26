package top.jpower.system.controller.api;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.system.api.dto.CityDTO;
import top.jpower.system.api.dto.ClientDTO;
import top.jpower.system.api.feign.SystemClient;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.system.dbs.entity.client.CoreClient;
import top.jpower.system.dbs.entity.client.TbCoreClient;
import top.jpower.system.dbs.entity.function.TbCoreDataScope;
import top.jpower.system.dbs.entity.org.TbCoreOrg;
import top.jpower.system.dbs.entity.role.TbCoreRole;
import top.jpower.system.dbs.entity.tenant.TbCoreTenant;
import top.jpower.system.service.city.CoreCityService;
import top.jpower.system.service.client.CoreClientService;
import top.jpower.system.service.org.CoreOrgService;
import top.jpower.system.service.role.CoreDataScopeService;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.service.role.CoreRoleService;
import top.jpower.system.service.tenant.TenantService;

import java.util.List;

/**
 * 
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("feign/core")
@AllArgsConstructor
public class SystemClientController extends BaseController implements SystemClient {

    private CoreOrgService coreOrgService;
    private CoreClientService coreClientService;
    private CoreFunctionService coreFunctionService;
    private TenantService tenantService;
    private CoreDataScopeService coreDataScopeService;
    private CoreRoleService coreRoleService;
    private CoreCityService coreCityService;

    @Override
    @GetMapping("/org/queryChildById")
    public R<List<Long>> queryChildOrgById(@RequestParam Long id){
        return R.ok("查询成功",coreOrgService.queryChildById(id));
    }

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public R<List<String>> getUrlsByRoleIds(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return R.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds,clientCode));
    }

    @Override
    @GetMapping("/function/getMenuListByRole")
    public R<List<TbCoreFunction>> getMenuListByRole(@RequestParam List<Long> roleIds, @RequestParam String clientCode, @RequestParam(required = false) Long topMenuId) {
        return R.ok("查询成功",coreFunctionService.listMenuByRoleId(roleIds,clientCode, topMenuId, Boolean.FALSE));
    }

    @Override
    @GetMapping("/dataScope/getAllRoleDataScope")
    public R<List<TbCoreDataScope>> getAllRoleDataScope() {
        return R.ok("查询成功",coreDataScopeService.getAllRoleDataScope());
    }

    @Override
    @GetMapping("/dataScope/getDataScopeByRole")
    public R<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return R.ok("查询成功",coreDataScopeService.getDataScopeByRole(roleIds, clientCode));
    }

    @Override
    @GetMapping("/role/getRoleNameByIds")
    public R<List<String>> getRoleNameByIds(@RequestParam List<Long> roleIds) {
        return R.ok("查询成功",coreRoleService.listObjs(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getName).in(TbCoreRole::getId,roleIds), Fc::toStr));
    }

    @Override
    @GetMapping("/tenant/getTenantByCode")
    public R<TbCoreTenant> getTenantByCode(@RequestParam String tenantCode){
        return R.ok("查询成功",tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenantCode)));
    }

    @Override
    @GetMapping("/org/queryOrgById")
    public R<TbCoreOrg> queryOrgById(@RequestParam Long orgId) {
        return R.ok("查询成功",coreOrgService.getById(orgId));
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
        return R.data(coreFunctionService.getObj(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreFunction::getId)
                .eq(TbCoreFunction::getCode,code) , Fc::toLong));
    }
}
