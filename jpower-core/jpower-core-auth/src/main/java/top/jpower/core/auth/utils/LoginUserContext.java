package top.jpower.core.auth.utils;

import lombok.extern.slf4j.Slf4j;
import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;

import java.util.Collections;
import java.util.Objects;

/**
 * 当前登陆用户信息<br/>
 * 当前用户未登陆可获取到匿名用户或白名单
 *
 * @author mr.g
 **/
@Slf4j
public class LoginUserContext {

    /**
     * 返回用户登录名
	 *
	 * @author 郭丁志
     **/
    public static String getLoginId() {
        UserInfo user = get();
        if (user == null){
            return StringPool.EMPTY;
        }
        return user.getLoginId();
    }

    /**
     * 返回用户ID
	 *
	 * @author 郭丁志
     * @return java.lang.String
     **/
    public static Long getUserId() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getUserId();
    }

    /**
     * 返回部门ID
	 *
	 * @author mr.g
     * @return java.lang.String
     **/
    public static Long getOrgId() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getOrgId();
    }

    /**
     * 返回用户名称
	 *
	 * @author mr.g
     * @return java.lang.String
     **/
    public static String getUserName() {
        UserInfo user = get();
        if (user == null){
            return StringPool.EMPTY;
        }
        return user.getUserName();
    }

    /**
     * 返回用户信息
     *
     * @author mr.g
     * @return top.jpower.jpower.module.common.auth.UserInfo
     **/
    public static UserInfo get() {
        UserInfo user = ShieldUtil.getUser();

        if (Fc.isNull(user)){
            if (Fc.notNull(WebUtil.getRequest())){
                String header = Objects.requireNonNull(WebUtil.getRequest(), "未获取到HttpServletRequest").getHeader(TokenConstant.PASS_HEADER_NAME);
                if (Fc.isNotBlank(header)){
                    user = new UserInfo();

                    try { user.setClientCode(ShieldUtil.getClientCodeFromHeader()); }catch (Exception ignored){}
                    user.setLoginId(header);
                    user.setNickName(header);
                    user.setUserType(RoleConstant.ANONYMOUS_UESR_TYPE);
                    if (Fc.equals(header, RoleConstant.ANONYMOUS)){
                        user.setUserId(RoleConstant.ANONYMOUS_ID);
                        user.setIsSysUser(UserInfo.TABLE_USER_TYPE_CORE);
                        user.setUserName(RoleConstant.ANONYMOUS_NAME);
                        user.setRoleIds(Collections.singletonList(RoleConstant.ANONYMOUS_ID));
                    }else {
                        user.setUserId(0L);
                        user.setIsSysUser(UserInfo.TABLE_USER_TYPE_WHILT);
                        user.setUserName(header);
                    }
                }
            }
        }

        return user;
    }
}
