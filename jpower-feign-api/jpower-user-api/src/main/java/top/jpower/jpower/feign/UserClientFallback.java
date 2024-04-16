package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.ValidatePasswordDto;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsReturn;
import top.jpower.jpower.vo.UserVo;

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
            public ResponseData saveUser(TbCoreUser user) {
                log.error("调用saveUser失败，参数：user={} ，e={}", user, cause);
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
             * @param validatePasswordDto
             * @return
             * @author mr.g
             **/
            @Override
            public boolean validatePassword(ValidatePasswordDto validatePasswordDto) {
                log.error("调用validatePassword失败，参数：validatePasswordDto={}", validatePasswordDto);
                return false;
            }
        };
    }
}
