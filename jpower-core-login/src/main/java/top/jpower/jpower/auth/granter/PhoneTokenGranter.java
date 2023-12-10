package top.jpower.jpower.auth.granter;

import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.StringUtil;
import top.jpower.jpower.utils.TokenUtil;
import top.jpower.jpower.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static top.jpower.jpower.auth.granter.PhoneTokenGranter.GRANT_TYPE;


/**
 * @ClassName PhoneTokenGranter
 * @Description TODO 手机号登录默认实现类
 * @Author 郭丁志
 * @Date 2020-07-28 14:23
 * @Version 1.0
 */
@Component(GRANT_TYPE)
public class PhoneTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "phone";

    @Autowired
    private RedisUtil redisUtils;
    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        String phone = tokenParameter.getPhone();
        String phoneCode = tokenParameter.getPhoneCode();
        String tenantCode = tokenParameter.getTenantCode();
        // 获取验证码
        String redisCode = String.valueOf(redisUtils.get(CacheNames.PHONE_KEY + phone + tenantCode));
        // 判断验证码;
        if (phoneCode == null || !StringUtil.equalsIgnoreCase(redisCode, phoneCode)) {
            throw new BusinessException(TokenUtil.PHONE_NOT_CORRECT);
        }

        UserInfo userInfo = null;
        if (Fc.isNotBlank(phone)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getPhoneUserInfo(tokenParameter);
            }else {
                TbCoreUser result = UserCache.getUserByPhone(phone,tenantCode);
                return UserUtil.toUserInfo(result);
            }
        }
        return userInfo;
    }

}
