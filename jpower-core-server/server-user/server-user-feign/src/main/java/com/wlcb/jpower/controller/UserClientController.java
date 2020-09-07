package com.wlcb.jpower.controller;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.mp.support.Condition;
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

    @ApiOperation(value = "通过账号密码查询用户")
    @Override
    @GetMapping("/queryUserByLoginIdPwd")
    public ResponseData<TbCoreUser> queryUserByLoginIdPwd(@RequestParam String loginId, @RequestParam String password){
        return ReturnJsonUtil.ok("查询成功",coreUserService.getOne(Condition.<TbCoreUser>getQueryWrapper()
                .lambda().eq(TbCoreUser::getLoginId,loginId).eq(TbCoreUser::getPassword, DigestUtil.encrypt(password))));
    }

    @ApiOperation(value = "通过用户ID查询所有角色ID")
    @Override
    @GetMapping("/getRoleIdsByUserId")
    public ResponseData<List<String>> getRoleIds(@RequestParam String userId){
        List<String> list = coreUserRoleService.listObjs(Condition.<TbCoreUserRole>getQueryWrapper()
                .lambda().select(TbCoreUserRole::getRoleId).eq(TbCoreUserRole::getUserId,userId), Fc::toStr);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation(value = "更新用户登陆信息")
    @Override
    @PutMapping("/updateUserLoginInfo")
    public ResponseData updateUserLoginInfo(@RequestParam TbCoreUser user){
        return ReturnJsonUtil.status(coreUserService.updateById(user));
    }

    @ApiOperation(value = "通过第三方CODE查询")
    @Override
    @GetMapping("/queryUserByCode")
    public ResponseData<TbCoreUser> queryUserByCode(@RequestParam String otherCode){
        TbCoreUser user = coreUserService.getOne(Condition.<TbCoreUser>getQueryWrapper()
                .lambda().eq(TbCoreUser::getOtherCode,otherCode));
        return ReturnJsonUtil.ok("查询成功",user);
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
    public ResponseData<TbCoreUser> queryUserByPhone(@RequestParam String phone){
        TbCoreUser user = coreUserService.getOne(Condition.<TbCoreUser>getQueryWrapper()
                .lambda().eq(TbCoreUser::getTelephone,phone));
        return ReturnJsonUtil.ok("查询成功",user);
    }
}
