package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TokenGranterBuilder
 * @Description TODO 构造登录查询
 * @Author 郭丁志
 * @Date 2020-07-28 00:34
 * @Version 1.0
 */
@AllArgsConstructor
public class TokenGranterBuilder {

    /**
     * TokenGranter缓存池
     */
    private static Map<String, TokenGranter> granterPool = new ConcurrentHashMap<>();

    private static UserClient userClient;

    static {
        userClient = SpringUtil.getBean(UserClient.class);


        granterPool.put(PasswordTokenGranter.GRANT_TYPE, SpringUtil.getBean(PasswordTokenGranter.class));
        granterPool.put(CaptchaTokenGranter.GRANT_TYPE, SpringUtil.getBean(CaptchaTokenGranter.class));
        granterPool.put(RefreshTokenGranter.GRANT_TYPE, SpringUtil.getBean(RefreshTokenGranter.class));
        granterPool.put(OtherCodeTokenGranter.GRANT_TYPE, SpringUtil.getBean(OtherCodeTokenGranter.class));
        granterPool.put(PhoneTokenGranter.GRANT_TYPE, SpringUtil.getBean(PhoneTokenGranter.class));
    }

    /**
     * 获取TokenGranter
     *
     * @param grantType 授权类型
     * @return ITokenGranter
     */
    public static TokenGranter getGranter(String grantType) {
        TokenGranter tokenGranter = granterPool.get(Fc.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
        if (tokenGranter == null) {
            throw new BusinessException("授权类型不存在");
        } else {
            return tokenGranter;
        }
    }


    public static UserInfo toUserInfo(TbCoreUser result) {
        UserInfo userInfo = null;
        if(result != null){

            if (Fc.equals(result.getActivationStatus(), ConstantsEnum.YN01.N.getValue())){
                throw new BusinessException(TokenUtil.USER_NOT_ACTIVATION);
            }

//            List<String> list  = coreUserRoleDao.listObjs(Condition.<TbCoreUserRole>getQueryWrapper()
//                    .lambda().select(TbCoreUserRole::getRoleId).eq(TbCoreUserRole::getUserId,result.getId()));
            List<String> list  = userClient.getRoleIds(result.getId()).getData();
            userInfo = new UserInfo();
            userInfo.setUserId(result.getId());
            userInfo.setIsSysUser(0);
            userInfo.setOrgId(result.getOrgId());
            userInfo.setUserType(result.getUserType());
            userInfo.setTelephone(result.getTelephone());
            userInfo.setLoginId(result.getLoginId());
            userInfo.setUserName(result.getUserName());
            userInfo.setNickName(result.getNickName());
            userInfo.setOtherCode(result.getOtherCode());
            userInfo.setRoleIds(list);

            // TODO: 2020-07-28 登录成功要刷新用户登录数据
            result.setLastLoginTime(new Date());
            result.setLoginCount((result.getLoginCount()==null?0:result.getLoginCount())+1);
//            coreUserDao.updateById(result);
            userClient.updateUserLoginInfo(result);
        }
        return userInfo;
    }

}
