package top.jpower.system.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.city.TbCoreCity;
import top.jpower.system.dbs.entity.client.TbCoreClient;
import top.jpower.system.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.system.dbs.entity.org.TbCoreOrg;
import top.jpower.system.dbs.entity.role.TbCoreRole;
import top.jpower.system.dbs.entity.tenant.TbCoreTenant;
import top.jpower.system.api.feign.SystemClient;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.mp.support.Condition;
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
@ApiIgnore
@RestController
@RequestMapping("/core")
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
    public ResponseData<List<Long>> queryChildOrgById(@RequestParam Long id){
        return ReturnJsonUtil.ok("查询成功",coreOrgService.queryChildById(id));
    }

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public ResponseData<List<String>> getUrlsByRoleIds(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds,clientCode));
    }

    @Override
    @GetMapping("/function/getMenuListByRole")
    public ResponseData<List<TbCoreFunction>> getMenuListByRole(@RequestParam List<Long> roleIds, @RequestParam String clientCode, @RequestParam(required = false) Long topMenuId) {
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.listMenuByRoleId(roleIds,clientCode, topMenuId, Boolean.FALSE));
    }

    @Override
    @GetMapping("/dataScope/getAllRoleDataScope")
    public ResponseData<List<TbCoreDataScope>> getAllRoleDataScope() {
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getAllRoleDataScope());
    }

    @Override
    @GetMapping("/dataScope/getDataScopeByRole")
    public ResponseData<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam List<Long> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getDataScopeByRole(roleIds, clientCode));
    }

    @Override
    @GetMapping("/role/getRoleNameByIds")
    public ResponseData<List<String>> getRoleNameByIds(@RequestParam List<Long> roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreRoleService.listObjs(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getName).in(TbCoreRole::getId,roleIds), Fc::toStr));
    }

    @Override
    @GetMapping("/tenant/getTenantByCode")
    public ResponseData<TbCoreTenant> getTenantByCode(@RequestParam String tenantCode){
        return ReturnJsonUtil.ok("查询成功",tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenantCode)));
    }

    @Override
    @GetMapping("/org/queryOrgById")
    public ResponseData<TbCoreOrg> queryOrgById(@RequestParam Long orgId) {
        return ReturnJsonUtil.ok("查询成功",coreOrgService.getById(orgId));
    }

    @Override
    @GetMapping("/client/getClientByClientCode")
    public ResponseData<TbCoreClient> getClientByClientCode(@RequestParam String clientCode) {
        return ReturnJsonUtil.ok("查询成功",coreClientService.loadClientByClientCode(clientCode));
    }

    @Override
    @GetMapping("/city/getCityByCode")
    public ResponseData<TbCoreCity> getCityByCode(@RequestParam String code) {
        return ReturnJsonUtil.ok("查询成功",coreCityService.queryByCode(code));
    }

    @GetMapping("/menu/getIdByCode")
    public ResponseData<Long> getMenuIdByCode(@RequestParam String code) {
        return ReturnJsonUtil.data(coreFunctionService.getObj(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreFunction::getId)
                .eq(TbCoreFunction::getCode,code) , Fc::toLong));
    }
}
