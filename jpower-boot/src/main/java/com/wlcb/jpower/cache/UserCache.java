package com.wlcb.jpower.cache;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.service.CoreUserRoleService;
import com.wlcb.jpower.service.CoreUserService;
import com.wlcb.jpower.service.impl.CoreUserRoleServiceImpl;
import com.wlcb.jpower.service.impl.CoreUserServiceImpl;
import com.wlcb.jpower.vo.UserVo;

import java.util.List;

/**
 * @ClassName UserCache
 * @Description TODO 用户缓存
 * @Author 郭丁志
 * @Version 1.0
 */
public class UserCache {

    private static CoreUserService userService;
    private static CoreUserRoleService userRoleService;

    static {
        userService = SpringUtil.getBean(CoreUserServiceImpl.class);
        userRoleService = SpringUtil.getBean(CoreUserRoleServiceImpl.class);
    }

    public static ResponseData saveAdmin(TbCoreUser user, String roleId) {
        return userService.saveAdmin(user,roleId)?ReturnJsonUtil.ok("用户创建成功"):ReturnJsonUtil.fail("用户创建失败");
    }

    public static TbCoreUser getUserByPhone(String telephone, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_PHPNE_KEY,telephone,() -> {
            TbCoreUser user = userService.selectByPhone(telephone,tenantCode);
            return user;
        });
    }

    public static TbCoreUser getUserByLoginId(String loginId, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_LOGINID_KEY,loginId,() -> {
            TbCoreUser user = userService.selectUserLoginId(loginId,tenantCode);
            return user;
        });
    }

    public static TbCoreUser queryUserByLoginIdPwd(String loginId,String password, String tenantCode) {
        TbCoreUser user = getUserByLoginId(loginId,tenantCode);
        if (Fc.notNull(user) && Fc.equals(DigestUtil.encrypt(password),user.getPassword())){
            return user;
        }
        return null;
    }

    public static List<String> getRoleIds(String userId) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_ROLEID_USERID_KEY,userId,() -> {
            List<String> list = userRoleService.queryRoleIds(userId);
            return list;
        });
    }

    public static TbCoreUser getUserByCode(String otherCode, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_OTHERCODE_KEY,otherCode,() -> {
            TbCoreUser user = userService.selectUserByOtherCode(otherCode,tenantCode);
            return user;
        });
    }

    public static UserVo getById(String userId) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_OTHERCODE_KEY,userId,() -> userService.selectUserById(userId));
    }
}
