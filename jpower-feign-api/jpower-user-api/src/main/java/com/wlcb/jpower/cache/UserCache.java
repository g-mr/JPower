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
 * 用户缓存
 *
 * @author mr.g
 **/
public class UserCache {

    private static UserClient userClient;

    static {
        userClient = SpringUtil.getBean(UserClient.class);
    }

    /**
     * 通过手机号查询用户
     *
     * @author mr.g
     * @param telephone 手机号
     * @param tenantCode 租户CODE
     * @return 用户信息
     **/
    public static TbCoreUser getUserByPhone(String telephone, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_PHPNE_KEY,telephone,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByPhone(telephone,tenantCode);
            return responseData.getData();
        });
    }

    /**
     * 通过账号查询用户
     *
     * @author mr.g
     * @param loginId 账号
     * @param tenantCode 租户CODE
     * @return 用户信息
     **/
    public static TbCoreUser getUserByLoginId(String loginId, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_LOGINID_KEY,loginId,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByLoginId(loginId,tenantCode);
            return responseData.getData();
        });
    }

    /**
     * 通过账号密码查询用户
     *
     * @author mr.g
     * @param loginId 账号
     * @param password 密码
     * @param tenantCode 租户CODE
     * @return 用户信息
     **/
    public static TbCoreUser queryUserByLoginIdPwd(String loginId,String password, String tenantCode) {
        TbCoreUser user = getUserByLoginId(loginId,tenantCode);
        if (Fc.notNull(user) && DigestUtil.checkPwd(password,user.getPassword())){
            return user;
        }
        return null;
    }

    /**
     * 获取用户的所有角色ID
     *
     * @author mr.g
     * @param userId
     * @return 角色ID列表
     **/
    public static List<String> getRoleIds(String userId) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_ROLEID_KEY,userId,() -> {
            ResponseData<List<String>> responseData = userClient.getRoleIds(userId);
            return responseData.getData();
        });
    }

    /**
     * 通过第三方CODE获取用户
     *
     * @author mr.g
     * @param otherCode 三方CODE
     * @param tenantCode 租户
     * @return 用户信息
     **/
    public static TbCoreUser getUserByCode(String otherCode, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_OTHERCODE_KEY,otherCode,() -> {
            ResponseData<TbCoreUser> responseData = userClient.queryUserByCode(otherCode,tenantCode);
            return responseData.getData();
        });
    }

    /**
     * 通过ID获取用户信息
     *
     * @author mr.g
     * @param userId 用户ID
     * @return 用户信息
     **/
    public static UserVo getById(String userId) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_DETAIL_KEY,userId,() -> {
            ResponseData<UserVo> responseData = userClient.get(userId);
            return responseData.getData();
        });
    }

}
