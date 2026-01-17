package top.jpower.user.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.util.constants.ReturnConstants;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.jpower.dbs.entity.CorePost;
import top.jpower.jpower.dbs.entity.CoreUser;
import top.jpower.user.api.dto.ValidatePasswordDTO;
import top.jpower.jpower.vo.UserVO;

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
            public ResponseData<CoreUser> queryUserByLoginId(String loginId, String tenantCode) {
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
            public ResponseData<CoreUser> queryUserByCode(String otherCode, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<UserVO> get(Long id) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData<CoreUser> queryUserByPhone(String phone, String tenantCode) {
                return ReturnJsonUtil.fail("查询失败");
            }

            @Override
            public ResponseData saveUser(CoreUser user) {
                log.error("调用saveUser失败，参数：user={} ，e={}", user, cause);
                return ReturnJsonUtil.print(ReturnConstants.RECODE_API, cause.getMessage(), false);
            }

            @Override
            public ResponseData<List<CoreUser>> listByUserType(Integer userType) {
                log.error("调用listByUserType失败，参数：userType={}", userType);
                return ReturnJsonUtil.print(ReturnConstants.RECODE_API, cause.getMessage(), false);
            }

            @Override
            public ResponseData<CorePost> queryPostById(Long postId) {
                log.error("调用queryPostById失败，参数：postId={}", postId);
                return ReturnJsonUtil.print(ReturnConstants.RECODE_API, cause.getMessage(), false);
            }

            /**
             * 验证账号密码是否正确
             *
             * @param validatePasswordDto
             * @return
             * @author mr.g
             **/
            @Override
            public boolean validatePassword(ValidatePasswordDTO validatePasswordDto) {
                log.error("调用validatePassword失败，参数：validatePasswordDto={}", validatePasswordDto);
                return false;
            }
        };
    }
}
