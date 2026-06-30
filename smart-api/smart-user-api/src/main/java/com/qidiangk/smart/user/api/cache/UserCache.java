package com.qidiangk.smart.user.api.cache;

import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.SpringUtil;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;
import com.qidiangk.smart.user.api.feign.UserClient;

import java.util.List;

/**
 * 用户缓存
 *
 * @author mr.g
 **/
public class UserCache {

    private static final UserClient USER_CLIENT;

    static {
        USER_CLIENT = SpringUtil.getBean(UserClient.class);
    }

    /**
     * 通过手机号查询用户
     *
     * @author mr.g
     * @param telephone 手机号
     * @param tenantCode 租户CODE
     * @return 用户信息
     **/
    public static CoreUserDTO getUserByPhone(String telephone, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_PHPNE_KEY, telephone,() -> {
            R<CoreUserDTO> r = USER_CLIENT.queryUserByPhone(telephone,tenantCode);
            return r.getData();
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
	public static CoreUserDTO getUserByLoginId(String loginId, String tenantCode) {
		return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_LOGINID_KEY,loginId,() -> {
			R<CoreUserDTO> r = USER_CLIENT.queryUserByLoginId(loginId,tenantCode);
			return r.getData();
		});
	}

    /**
     * 获取用户的所有角色ID
     *
     * @author mr.g
     * @param userId 用户ID
     * @return 角色ID列表
     **/
    public static List<Long> getRoleIds(Long userId) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_ROLEID_KEY,userId,() -> {
            R<List<Long>> r = USER_CLIENT.getRoleIds(userId);
            return r.getData();
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
    public static CoreUserDTO getUserByCode(String otherCode, String tenantCode) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_OTHERCODE_KEY,otherCode,() -> {
            R<CoreUserDTO> r = USER_CLIENT.queryUserByCode(otherCode,tenantCode);
            return r.getData();
        });
    }

    /**
     * 通过ID获取用户信息
     *
     * @author mr.g
     * @param userId 用户ID
     * @return 用户信息
     **/
    public static CoreUserDTO getById(Long userId) {
        return CacheUtil.get(CacheNames.USER_KEY,CacheNames.USER_DETAIL_KEY, userId,() -> {
            R<CoreUserDTO> r = USER_CLIENT.get(userId);
            return r.getData();
        });
    }

}
