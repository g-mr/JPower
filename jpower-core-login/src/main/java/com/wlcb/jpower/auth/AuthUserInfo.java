package com.wlcb.jpower.auth;

import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.utils.AuthUtil;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郭丁志
 * @Description //TODO 授权需求实现，如果有业务上的特殊需求，继承该类实现即可；继承的必须加上@Configuration注解
 * @date 22:05 2020/8/6 0006
 */
@Configuration
public interface AuthUserInfo {

    /**
     * @author 郭丁志
     * @Description //TODO 密码登陆各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getPasswordUserInfo(TokenParameter tokenParameter){
        TbCoreUser result = UserCache.queryUserByLoginIdPwd(tokenParameter.getLoginId(),tokenParameter.getPassWord(),tokenParameter.getTenantCode());
        return AuthUtil.toUserInfo(result);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 验证码登陆各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getCaptchaUserInfo(TokenParameter tokenParameter){
        if (Fc.isNoneBlank(tokenParameter.getLoginId(), tokenParameter.getPassWord())) {
            return getPasswordUserInfo(tokenParameter);
        }
        return null;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 第三方Code各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getOtherCodeUserInfo(TokenParameter tokenParameter){
//        String otherCode = tokenParameter.getStr("otherCode");
//        String tenantCode = tokenParameter.getStr("tenantCode");
//
//        TbCoreUser result = UserCache.getUserByCode(otherCode,tenantCode);
//        return TokenGranterBuilder.toUserInfo(result);
        throw new BusinessException("暂不支持第三方验证码登录");
    }

    /**
     * @author 郭丁志
     * @Description //TODO 刷新token各自业务实现
     * @date 22:55 2020/8/6 0006
     * @param userType 用户类型，扩展字典，根据业务自行使用
     * @param userId 用户主键ID
     * @return UserInfo 只需要实现获取UserInfo即可，token的刷新不用去管
     */
    default UserInfo getRefreshUserInfo(String userType,String userId){
        TbCoreUser result = UserCache.getById(userId);
        return AuthUtil.toUserInfo(result);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 手机号登录
     * @Date 08:40 2020-08-21
     * @Param [tokenParameter]
     * @return com.wlcb.jpower.module.common.auth.UserInfo
     **/
    default UserInfo getPhoneUserInfo(TokenParameter tokenParameter){
        TbCoreUser result = UserCache.getUserByPhone(tokenParameter.getPhone(),tokenParameter.getTenantCode());
        return AuthUtil.toUserInfo(result);
    }

}
