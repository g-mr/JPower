package com.wlcb.jpower.cache;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;

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
}
