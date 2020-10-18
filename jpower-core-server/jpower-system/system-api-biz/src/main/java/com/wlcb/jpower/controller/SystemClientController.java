package com.wlcb.jpower.controller;

import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.feign.SystemClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.client.CoreClientService;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.service.role.CoreFunctionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @Override
    @GetMapping("/org/queryChildById")
    public ResponseData<List<String>> queryChildOrgById(@RequestParam String id){
        List<String> list = coreOrgService.queryChildById(id);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @Override
    @GetMapping("/function/getUrlsByRoleIds")
    public ResponseData<List<Object>> getUrlsByRoleIds(@RequestParam String roleIds) {
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.getUrlsByRoleIds(roleIds));
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
