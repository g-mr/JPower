package top.jpower.jpower.utils;

import top.jpower.common.enums.YN01Enum;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;

/**
 * @author mr.g
 * @date 2020-11-05 16:45
 */
public class UserUtil {

    public static UserInfo toUserInfo(TbCoreUser result) {
        UserInfo userInfo = null;
        if(Fc.notNull(result)){

            if (Fc.equalsValue(result.getActivationStatus(), YN01Enum.N.getValue())){
                throw new BusinessException(TokenUtil.USER_NOT_ACTIVATION);
            }

            userInfo = new UserInfo();
            userInfo.setUserId(result.getId());
            userInfo.setIsSysUser(UserInfo.TABLE_USER_TYPE_CORE);
            userInfo.setAvatar(result.getAvatar());
            userInfo.setOrgId(result.getOrgId());
            userInfo.setOrgName(SystemCache.getOrgName(result.getOrgId()));
            userInfo.setUserType(result.getUserType());
            userInfo.setTelephone(result.getTelephone());
            userInfo.setLoginId(result.getLoginId());
            userInfo.setUserName(result.getUserName());
            userInfo.setNickName(result.getNickName());
            userInfo.setOtherCode(result.getOtherCode());
            userInfo.setTenantCode(result.getTenantCode());
            userInfo.setRoleIds(UserCache.getRoleIds(result.getId()));
            userInfo.setPostCode(result.getPostCode());
            userInfo.setEmail(result.getEmail());
            userInfo.setAddress(result.getAddress());
            userInfo.setBirthday(result.getBirthday());
            userInfo.setIdType(result.getIdType());
            userInfo.setIdNo(result.getIdNo());
            userInfo.setLastLoginTime(result.getLastLoginTime());
            userInfo.setLoginCount(result.getLoginCount());
            userInfo.setChildOrgId(SystemCache.getChildIdOrgById(result.getOrgId()));
            CacheUtil.clear(CacheNames.USER_KEY);
        }
        return userInfo;
    }
}
