package com.wlcb.jpower.feign;

import com.wlcb.jpower.dbs.entity.TbCorePost;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

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
                log.error("调用queryUserByLoginIdPwd失败，参数：loginId={}，e={}", loginId, cause);
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<List<Long>> getRoleIds(Long userId) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData updateUserLoginInfo(@RequestParam Long userId) {
                log.error("调用updateUserLoginInfo失败，参数：{}，e={}", userId, cause);
                return ReturnJsonUtil.fail("更新失败");
            }

            @Override
            public ResponseData<TbCoreUser> queryUserByCode(String otherCode, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<UserVo> get(Long id) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<TbCoreUser> queryUserByPhone(String phone, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData saveUser(TbCoreUser user, Long roleId) {
                log.error("调用saveUser失败，参数：user={}，roleId={} ，e={}", user, roleId, cause);
                return ReturnJsonUtil.print(ConstantsReturn.RECODE_API, cause.getMessage(), false);
            }

            @Override
            public ResponseData<List<TbCoreUser>> listByUserType(Integer userType) {
                log.error("调用listByUserType失败，参数：userType={}", userType);
                return ReturnJsonUtil.print(ConstantsReturn.RECODE_API, cause.getMessage(), false);
            }

            @Override
            public ResponseData<TbCorePost> queryPostById(Long postId) {
                log.error("调用queryPostById失败，参数：postId={}", postId);
                return ReturnJsonUtil.print(ConstantsReturn.RECODE_API, cause.getMessage(), false);
            }

            /**
             * 验证帐号密码是否正确
             *
             * @param account
             * @param password
             * @param tenantCode
             * @return
             * @author mr.g
             **/
            @Override
            public boolean validatePassword(String account, String password, String tenantCode) {
                log.error("调用validatePassword失败，参数：account={},password={},tenantCode={}", account, password, tenantCode);
                return false;
            }
        };
    }
}
