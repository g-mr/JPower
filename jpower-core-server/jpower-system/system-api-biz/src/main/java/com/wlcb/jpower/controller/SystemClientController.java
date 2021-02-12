package com.wlcb.jpower.controller;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.feign.SystemClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.client.CoreClientService;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.service.role.CoreDataScopeService;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.service.role.CoreRoleService;
import com.wlcb.jpower.service.tenant.TenantService;
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
public class SystemClientController implements SystemClient {

    private CoreOrgService coreOrgService;
    private CoreClientService coreClientService;
    private CoreFunctionService coreFunctionService;
    private TenantService tenantService;
    private CoreDataScopeService coreDataScopeService;
    private CoreRoleService coreRoleService;

    @Override
    @GetMapping("/org/queryChildById")
    public ResponseData<List<String>> queryChildOrgById(@RequestParam String id){
        return ReturnJsonUtil.ok("查询成功",coreOrgService.queryChildById(id));
    }

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public ResponseData<List<Object>> getUrlsByRoleIds(@RequestParam List<String> roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds));
    }

    @Override
    @GetMapping("/function/getMenuListByRole")
    public ResponseData<List<TbCoreFunction>> getMenuListByRole(@RequestParam List<String> roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.getMenuListByRole(roleIds));
    }

    @Override
    @GetMapping("/dataScope/getAllRoleDataScope")
    public ResponseData<List<TbCoreDataScope>> getAllRoleDataScope() {
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getAllRoleDataScope());
    }

    @Override
    @GetMapping("/dataScope/getDataScopeByRole")
    public ResponseData<List<TbCoreDataScope>> getDataScopeByRole(@RequestParam List<String> roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreDataScopeService.getDataScopeByRole(roleIds));
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

}
