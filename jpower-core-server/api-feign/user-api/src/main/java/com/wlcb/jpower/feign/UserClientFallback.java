package com.wlcb.jpower.feign;

import com.wlcb.jpower.entity.UserDto;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName UserClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:11
 * @Version 1.0
 */
@Component
public class UserClientFallback implements UserClient {
    @Override
    public ResponseData<UserDto> queryUserByLoginIdPwd(String loginId, String password) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<List<String>> getRoleIds(String userId) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData updateUserLoginInfo(UserDto user) {
        return ReturnJsonUtil.fail("更新失败");
    }

    @Override
    public ResponseData<UserDto> queryUserByCode(String otherCode) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<UserDto> get(String id) {
        return ReturnJsonUtil.fail("查询失败");
    }

    @Override
    public ResponseData<UserDto> queryUserByPhone(String phone) {
        return ReturnJsonUtil.fail("查询失败");
    }
}
