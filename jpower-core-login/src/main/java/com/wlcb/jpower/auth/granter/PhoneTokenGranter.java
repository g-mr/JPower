package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @ClassName PhoneTokenGranter
 * @Description TODO 手机号登录默认实现类
 * @Author 郭丁志
 * @Date 2020-07-28 14:23
 * @Version 1.0
 */
@Component
public class PhoneTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "phone";

    @Autowired
    private RedisUtil redisUtils;
    @Resource
    private UserClient userClient;
    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

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

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getPhoneUserInfo(tokenParameter);
            }else {
                TbCoreUser result = userClient.queryUserByPhone(phone).getData();
                return TokenGranterBuilder.toUserInfo(result);
            }
        }
        return userInfo;
    }

}
