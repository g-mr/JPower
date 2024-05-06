package top.jpower.jpower.module.dbs.config;

import lombok.extern.slf4j.Slf4j;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.jpower.module.common.auth.RoleConstant;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.utils.ShieldUtil;

import java.util.Collections;
import java.util.Objects;

import static top.jpower.jpower.module.common.auth.RoleConstant.ANONYMOUS_UESR_TYPE;

/**
 * 当前登陆用户信息<br/>
 * 当前用户未登陆可获取到匿名用户或白名单
 *
 * @author mr.g
 **/
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
            return StringPool.EMPTY;
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
    public static Long getUserId() {
        UserInfo user = get();
        if (user == null){
            return null;
        }
        return user.getUserId();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回部门ID
     * @Date 17:12 2020-07-09
     * @Param []
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
     * @Author 郭丁志
     * @Description //TODO 返回用户名称
     * @Date 17:12 2020-07-09
     * @Param []
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
                    user.setUserType(ANONYMOUS_UESR_TYPE);
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
