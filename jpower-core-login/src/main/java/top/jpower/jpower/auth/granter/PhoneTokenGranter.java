package top.jpower.jpower.auth.granter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.feign.SmsClient;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.utils.TokenUtil;
import top.jpower.jpower.utils.UserUtil;

import static top.jpower.jpower.auth.granter.PhoneTokenGranter.GRANT_TYPE;
import static top.jpower.jpower.module.common.utils.constants.JpowerConstants.VALIDATE_SMS_CODE;


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

    @Autowired(required = false)
    private AuthUserInfo authUserInfo;
    @Autowired
    private SmsClient smsClient;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        String phone = tokenParameter.getPhone();
        String phoneCode = tokenParameter.getPhoneCode();
        String tenantCode = tokenParameter.getTenantCode();
        if (!smsClient.validate(VALIDATE_SMS_CODE, phone, phoneCode)){
            throw new BusinessException(TokenUtil.PHONE_NOT_CORRECT);
        }

        if (Fc.isNotBlank(phone)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getPhoneUserInfo(tokenParameter);
            }else {
                TbCoreUser result = UserCache.getUserByPhone(phone,tenantCode);
                JpowerAssert.notNull(result, JpowerError.Business, "用户不存在");
                return UserUtil.toUserInfo(result);
            }
        }
        return null;
    }

}
