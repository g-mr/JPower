package top.jpower.user.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.util.constants.ReturnConstants;
import top.jpower.core.util.rsp.R;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.dto.ValidatePasswordDTO;

import java.util.List;

/**
 * USER 熔断
 *
 * @author mr.g
 */
@Component
@Slf4j
public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public R<CoreUserDTO> queryUserByLoginId(String loginId, String tenantCode) {
                log.error("调用queryUserByLoginIdPwd失败，参数：loginId={}，e={}", loginId, cause);
                return R.fail("查询失败");
            }

            @Override
            public R<List<Long>> getRoleIds(Long userId) {
                return R.fail("查询失败");
            }

            @Override
            public R<Boolean> updateLoginCount(@RequestParam Long userId) {
                log.error("调用updateUserLoginInfo失败，参数：{}，e={}", userId, cause);
                return R.fail("更新失败");
            }

            @Override
            public R<CoreUserDTO> queryUserByCode(String otherCode, String tenantCode) {
                return R.fail("查询失败");
            }

            @Override
            public R<CoreUserDTO> get(Long id) {
                return R.fail("查询失败");
            }

            @Override
            public R<CoreUserDTO> queryUserByPhone(String phone, String tenantCode) {
                return R.fail("查询失败");
            }

            @Override
            public R<Long> saveUser(CoreUserDTO user) {
                log.error("调用saveUser失败，参数：user={} ，e={}", user, cause);
                return R.print(ReturnConstants.RECODE_API, cause.getMessage(), false);
            }

            /**
             * 验证账号密码是否正确
             *
             * @param validatePasswordDto
             * @return
             * @author mr.g
             **/
            @Override
            public R<Boolean> validatePassword(ValidatePasswordDTO validatePasswordDto) {
                log.error("调用validatePassword失败，参数：validatePasswordDto={}", validatePasswordDto, cause);
                return R.fail("请求失败", false);
            }
        };
    }
}
