package com.wlcb.jpower.controller;

import com.wlcb.jpower.feign.SystemClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.service.role.CoreFunctionService;
import io.swagger.annotations.ApiOperation;
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
    private CoreFunctionService coreFunctionService;

    @ApiOperation(value = "加载下级所有部门ID")
    @Override
    @GetMapping("/org/queryChildById")
    public ResponseData<List<String>> queryChildById(@RequestParam String id){
        List<String> list = coreOrgService.queryChildById(id);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation(value = "把当前用户的权限设置到redis")
    @Override
    @PostMapping("/function/putRedisAllFunctionByRoles")
    public boolean putRedisAllFunctionByRoles(@RequestParam List<String> roleIds, @RequestParam Long expiresIn, @RequestParam String accessToken){
        coreFunctionService.putRedisAllFunctionByRoles(roleIds,expiresIn,accessToken);
        return true;
    }

}
