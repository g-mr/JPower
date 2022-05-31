package com.wlcb.jpower.cache;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.vo.UserVo;

import java.util.List;

/**
 * @ClassName UserCache
 * @Description TODO 用户缓存
 * @Author 郭丁志
 * @Version 1.0
 */
public class UserCache {

    private static UserClient userClient;

    static {
        userClient = SpringUtil.getBean(UserClient.class);
    }

    public static ResponseData saveAdmin(TbCoreUser user, String roleId) {
        return userClient.saveAdmin(user,roleId);
    }

    public static TbCoreUser getUserByPhone(String telephone, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_PHPNE_KEY,telephone,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByPhone(telephone,tenantCode);
            return responseData.getData();
        });
    }

    public static TbCoreUser getUserByLoginId(String loginId, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_LOGINID_KEY,loginId,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByLoginId(loginId,tenantCode);
            return responseData.getData();
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
            ResponseData<List<String>> responseData = userClient.getRoleIds(userId);
            return responseData.getData();
        });
    }

    public static TbCoreUser getUserByCode(String otherCode, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_OTHERCODE_KEY,otherCode,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByCode(otherCode,tenantCode);
            return responseData.getData();
        });
    }

    public static UserVo getById(String userId) {
        return CacheUtil.get(CacheNames.USER_REDIS_CACHE,CacheNames.USER_OTHERCODE_KEY,userId,() -> {
            ResponseData<UserVo> responseData = userClient.get(userId);
            return responseData.getData();
        });
    }

}
