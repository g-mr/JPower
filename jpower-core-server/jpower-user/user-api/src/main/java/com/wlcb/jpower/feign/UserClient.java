package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserClient
 * @Description TODO 用户
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:00
 * @Version 1.0
 */
@FeignClient(value = AppConstant.JPOWER_USER, fallbackFactory = UserClientFallback.class, path = "/core/user")
public interface UserClient {

    @GetMapping("/queryUserByLoginId")
    ResponseData<TbCoreUser> queryUserByLoginId(@RequestParam String loginId, @RequestParam String tenantCode);

    @GetMapping("/getRoleIdsByUserId")
    ResponseData<List<String>> getRoleIds(@RequestParam String userId);

    @PutMapping("/updateUserLoginInfo")
    ResponseData updateUserLoginInfo(@RequestBody TbCoreUser user);

    @GetMapping("/queryUserByCode")
    ResponseData<TbCoreUser> queryUserByCode(@RequestParam String otherCode, @RequestParam String tenantCode);

    @GetMapping("/get")
    ResponseData<UserVo> get(@RequestParam String id);

    @GetMapping("/queryUserByPhone")
    ResponseData<TbCoreUser> queryUserByPhone(@RequestParam String phone,@RequestParam String tenantCode);

    @PostMapping("/saveAdmin")
    ResponseData saveAdmin(@RequestBody TbCoreUser user,@RequestParam String roleId);

    @GetMapping("/listByUserType")
    ResponseData<List<TbCoreUser>> listByUserType(@RequestParam Integer userType);
}
