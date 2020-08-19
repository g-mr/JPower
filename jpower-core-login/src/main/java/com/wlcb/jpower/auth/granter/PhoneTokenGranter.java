package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @ClassName PhoneTokenGranter
 * @Description TODO 手机号登录默认实现类
 * @Author 郭丁志
 * @Date 2020-07-28 14:23
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class PhoneTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "phone";

    private RedisUtil redisUtils;
    private TbCoreUserDao coreUserDao;

    @Override
    public UserInfo grant(ChainMap tokenParameter) {
        String phone = tokenParameter.getStr("phone");
        String phoneCode = tokenParameter.getStr("phoneCode");
        // 获取验证码
        String redisCode = String.valueOf(redisUtils.get(CacheNames.PHONE_KEY + phone));
        // 判断验证码
        if (phoneCode == null || !StringUtil.equalsIgnoreCase(redisCode, phoneCode)) {
            throw new BusinessException(TokenUtil.PHONE_NOT_CORRECT);
        }

        UserInfo userInfo = null;
        if (Fc.isNotBlank(phone)) {
            // 获取用户类型
            String userType = tokenParameter.getStr("userType");
            //不同的用户类型都可以在这里写逻辑，或者重写这个类的方法
            if (Fc.equals(userType,"web")){
                TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
                        .lambda().eq(TbCoreUser::getTelephone,phone));
                return TokenGranterBuilder.toUserInfo(result);
            }else {
                TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
                        .lambda().eq(TbCoreUser::getTelephone,phone));
                return TokenGranterBuilder.toUserInfo(result);
            }
        }
        return userInfo;
    }

}
