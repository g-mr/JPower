package top.jpower.jpower.controller;

import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.jpower.feign.SystemClient;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.city.CoreCityService;
import top.jpower.jpower.service.client.CoreClientService;
import top.jpower.jpower.service.org.CoreOrgService;
import top.jpower.jpower.service.role.CoreDataScopeService;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreRoleService;
import top.jpower.jpower.service.tenant.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author mr.gmac
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
    public ResponseData<List<String>> queryChildOrgById(@RequestParam String id){
        return ReturnJsonUtil.ok("查询成功",coreOrgService.queryChildById(id));
    }

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public ResponseData<List<String>> getUrlsByRoleIds(@RequestParam List<String> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds,clientCode));
    }

    @Override
    @GetMapping("/function/getMenuListByRole")
    public ResponseData<List<TbCoreFunction>> getMenuListByRole(@RequestParam List<String> roleIds, @RequestParam String clientCode, @RequestParam(required = false) String topMenuId) {
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.listMenuByRoleId(roleIds,clientCode, topMenuId, Boolean.FALSE));
    }

    @Override
    @GetMapping("/dataScope/getAllRoleDataScope")
    public ResponseData<List<TbCoreDataScope>> getAllRoleDataScope() {
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getAllRoleDataScope());
    }

    @Override
    @GetMapping("/dataScope/getDataScopeByRole")
    public ResponseData<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam List<String> roleIds, @RequestParam(required = false) String clientCode) {
        clientCode = Fc.isBlank(clientCode) ? ShieldUtil.getClientCodeFromHeader() : clientCode;
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getDataScopeByRole(roleIds, clientCode));
    }

    @Override
    @GetMapping("/role/getRoleNameByIds")
    public ResponseData<List<String>> getRoleNameByIds(@RequestParam List<String> roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreRoleService.listObjs(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getName).in(TbCoreRole::getId,roleIds), Fc::toStr));
    }

    @Override
    @GetMapping("/tenant/getTenantByCode")
    public ResponseData<TbCoreTenant> getTenantByCode(@RequestParam String tenantCode){
        return ReturnJsonUtil.ok("查询成功",tenantService.getOne(Condition.<TbCoreTenant>getQueryWrapper().lambda().eq(TbCoreTenant::getTenantCode,tenantCode)));
    }

    @Override
    @GetMapping("/org/queryOrgById")
    public ResponseData<TbCoreOrg> queryOrgById(@RequestParam String orgId) {
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
    public ResponseData<String> getMenuIdByCode(@RequestParam String code) {
        return ReturnJsonUtil.data(coreFunctionService.getObj(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreFunction::getId)
                .eq(TbCoreFunction::getCode,code) , Fc::toStr));
    }
}
