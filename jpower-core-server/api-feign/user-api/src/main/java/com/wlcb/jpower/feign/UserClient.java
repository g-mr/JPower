package com.wlcb.jpower.feign;

import com.wlcb.jpower.entity.UserDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName UserClient
 * @Description TODO 用户
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:00
 * @Version 1.0
 */
@FeignClient(value = AppConstant.JPOWER_USER, fallback = UserClientFallback.class, path = "/core/user")
public interface UserClient {

    @GetMapping("/queryUserByLoginIdPwd")
    ResponseData<UserDto> queryUserByLoginIdPwd(@RequestParam String loginId, @RequestParam String password);

    @GetMapping("/getRoleIdsByUserId")
    ResponseData<List<String>> getRoleIds(@RequestParam String userId);

    @PutMapping("/updateUserLoginInfo")
    ResponseData updateUserLoginInfo(@RequestParam UserDto user);

    @GetMapping("/queryUserByCode")
    ResponseData<UserDto> queryUserByCode(@RequestParam String otherCode);

    @GetMapping("/get")
    ResponseData<UserDto> get(@RequestParam String id);

    @GetMapping("/queryUserByPhone")
    ResponseData<UserDto> queryUserByPhone(@RequestParam String phone);
}
