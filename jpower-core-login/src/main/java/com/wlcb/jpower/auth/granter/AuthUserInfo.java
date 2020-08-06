package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;

/**
 * @author 郭丁志
 * @Description //TODO 授权需求实现，如果有业务上的特殊需求，继承该类实现即可
 * @date 22:05 2020/8/6 0006
 */
public interface AuthUserInfo {

    /**
     * @author 郭丁志
     * @Description //TODO 密码登陆各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getPasswordUserInfo(ChainMap tokenParameter){
        return null;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 验证码登陆各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getCaptchaUserInfo(ChainMap tokenParameter){
        return null;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 第三方Code各自业务实现
     * @date 22:43 2020/8/6 0006
     */
    default UserInfo getOtherCodeUserInfo(ChainMap tokenParameter){
        return null;
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
        return null;
    }
}
