package com.wlcb.jpower.module.dbs.config;

import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;

/**
 * @ClassName LoginUserContext
 * @Description TODO 存储当前登录用户
 * @Author 郭丁志
 * @Date 2020-07-09 17:09
 * @Version 1.0
 */
public class LoginUserContext {

    private static ThreadLocal<TbCoreUser> coreUser = new ThreadLocal<TbCoreUser>();
    private static ThreadLocal<String> ip = new ThreadLocal<String>();

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户登录名
     * @Date 17:12 2020-07-09
     * @Param []
     * @return java.lang.String
     **/
    public static String getLoginId() {
        TbCoreUser user = coreUser.get();
        if (user == null){
            return null;
        }
        return user.getLoginId();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回用户名称
     * @Date 17:12 2020-07-09
     * @Param []
     * @return java.lang.String
     **/
    public static String getUserName() {
        TbCoreUser user = coreUser.get();
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
    public static TbCoreUser get() {
        TbCoreUser user = coreUser.get();
        return user;
    }

    public static void set(TbCoreUser user) {
        coreUser.set(user);
    }

    public static String getIp() {
        return ip.get();
    }

    public static void setIp(String rIp) {
        ip.set(rIp);
    }

    public static void remove() {
        coreUser.remove();
        ip.remove();
    }

}
