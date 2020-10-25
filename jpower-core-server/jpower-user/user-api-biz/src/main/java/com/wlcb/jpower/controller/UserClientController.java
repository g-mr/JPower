package com.wlcb.jpower.controller;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.service.CoreUserRoleService;
import com.wlcb.jpower.service.CoreUserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName UserClient
 * @Description TODO 用户feign接口实现
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:00
 * @Version 1.0
 */
@ApiIgnore
@RestController
@RequestMapping("/core/user")
@AllArgsConstructor
public class UserClientController implements UserClient {

    private CoreUserService coreUserService;
    private CoreUserRoleService coreUserRoleService;

    @ApiOperation(value = "通过账号查询用户")
    @Override
    @GetMapping("/queryUserByLoginId")
    public ResponseData<TbCoreUser> queryUserByLoginId(@RequestParam String loginId, @RequestParam String tenantCode){
        return ReturnJsonUtil.ok("查询成功",coreUserService.selectUserLoginId(loginId,tenantCode));
    }

    @ApiOperation(value = "通过用户ID查询所有角色ID")
    @Override
    @GetMapping("/getRoleIdsByUserId")
    public ResponseData<List<String>> getRoleIds(@RequestParam String userId){
        return ReturnJsonUtil.ok("查询成功",coreUserRoleService.queryRoleIds(userId));
    }

    @ApiOperation(value = "更新用户登陆信息")
    @Override
    @PutMapping("/updateUserLoginInfo")
    public ResponseData updateUserLoginInfo(@RequestBody TbCoreUser user){
        return ReturnJsonUtil.status(coreUserService.updateLoginInfo(user));
    }

    @ApiOperation(value = "通过第三方CODE查询")
    @Override
    @GetMapping("/queryUserByCode")
    public ResponseData<TbCoreUser> queryUserByCode(@RequestParam String otherCode, @RequestParam String tenantCode){
        return ReturnJsonUtil.ok("查询成功",coreUserService.selectUserByOtherCode(otherCode,tenantCode));
    }

    @ApiOperation("查询用户详情")
    @Override
    @GetMapping(value = "/get")
    public ResponseData<TbCoreUser> get(@RequestParam String id){
        TbCoreUser user = coreUserService.selectUserById(id);
        return ReturnJsonUtil.ok("查询成功", user);
    }

    @ApiOperation(value = "通过手机号查询用户")
    @Override
    @GetMapping("/queryUserByPhone")
    public ResponseData<TbCoreUser> queryUserByPhone(@RequestParam String phone,@RequestParam String tenantCode){
        TbCoreUser user = coreUserService.selectByPhone(phone,tenantCode);
        return ReturnJsonUtil.ok("查询成功",user);
    }

    @Override
    @PostMapping("/saveAdmin")
    public ResponseData saveAdmin(@RequestParam TbCoreUser user,@RequestParam String roleId) {
        return ReturnJsonUtil.status(coreUserService.saveAdmin(user,roleId));
    }
}
