package com.wlcb.jpower.feign;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName UserClientFallback
 * @Description TODO USER 熔断
 * @Author 郭丁志
 * @Date 2020/9/3 0003 1:11
 * @Version 1.0
 */
@Component
@Slf4j
public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public ResponseData<TbCoreUser> queryUserByLoginId(String loginId, String tenantCode) {
                log.error("调用queryUserByLoginIdPwd失败，参数：loginId={}，e={}",loginId,cause);
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<List<String>> getRoleIds(String userId) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData updateUserLoginInfo(TbCoreUser user) {
                log.error("调用updateUserLoginInfo失败，参数：{}，e={}", JSON.toJSONString(user),cause);
                return ReturnJsonUtil.fail("更新失败");
            }

            @Override
            public ResponseData<TbCoreUser> queryUserByCode(String otherCode, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<TbCoreUser> get(String id) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<TbCoreUser> queryUserByPhone(String phone, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }
        };
    }
}
