package com.wlcb.jpower.module.dbs.config;

import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName LoginUserContext
 * @Description TODO 获取当前用户
 * @Author 郭丁志
 * @Date 2020-07-09 17:09
 * @Version 1.0
 */
@Slf4j
public class LoginUserContext {

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户登录名
     * @Date 17:12 2020-07-09
     * @Param []
     * @return java.lang.String
     **/
    public static String getLoginId() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getLoginId();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户ID
     * @Date 17:12 2020-07-09
     * @Param []
     * @return java.lang.String
     **/
    public static String getUserId() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getUserId();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户名称
     * @Date 17:12 2020-07-09
     * @Param []
     * @return java.lang.String
     **/
    public static String getUserName() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getUserName();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户信息
     * @Date 17:12 2020-07-09
     * @Param []
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    public static UserInfo get() {
        UserInfo user = SecureUtil.getUser();

        if (Fc.isNull(user)){
            if (!Fc.isNull(WebUtil.getRequest())){
                String header = WebUtil.getRequest().getHeader(TokenConstant.PASS_HEADER_NAME);
                if (!Fc.isNull(header)){
                    user = new UserInfo();

                    try { user.setClientCode(SecureUtil.getClientCodeFromHeader()); }catch (Exception e){}
                    user.setLoginId(header);
                    user.setNickName(header);
                    if (Fc.equals(header, RoleConstant.ANONYMOUS)){
                        user.setUserId(RoleConstant.ANONYMOUS_ID);
                        user.setIsSysUser(UserInfo.TBALE_USER_TYPE_CORE);
                        user.setUserName(RoleConstant.ANONYMOUS_NAME);
                    }else {
                        user.setUserId(header);
                        user.setIsSysUser(UserInfo.TBALE_USER_TYPE_WHILT);
                        user.setUserName(header);
                    }
                }
            }
        }

        return user;
    }
}
