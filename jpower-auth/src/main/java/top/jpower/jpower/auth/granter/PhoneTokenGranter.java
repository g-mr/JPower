package top.jpower.jpower.auth.granter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.utils.TokenUtil;
import top.jpower.resource.api.dto.ValidateDTO;
import top.jpower.resource.api.feign.SmsClient;
import top.jpower.user.api.cache.UserCache;
import top.jpower.user.api.dto.CoreUserDTO;

import static top.jpower.core.util.constants.JpowerConstants.VALIDATE_SMS_CODE;
import static top.jpower.jpower.auth.granter.PhoneTokenGranter.GRANT_TYPE;


/**
 * 手机号登录默认实现类
 *
 * @author mr.g
 */
@Component(GRANT_TYPE)
public class PhoneTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "phone";

    @Autowired(required = false)
    private AuthUserInfo authUserInfo;
    @Autowired
    private SmsClient smsClient;

    @Override
    public CoreUserDTO grant(TokenParameter tokenParameter) {
        String phone = tokenParameter.getPhone();
        String phoneCode = tokenParameter.getPhoneCode();
        String tenantCode = tokenParameter.getTenantCode();
        if (!smsClient.validate(new ValidateDTO().setCode(VALIDATE_SMS_CODE).setPhone(phone).setPhoneCode(phoneCode)).isStatus()){
            throw new BusinessException(TokenUtil.PHONE_NOT_CORRECT);
        }

        if (Fc.isNotBlank(phone)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getPhoneUserInfo(tokenParameter);
            }else {
				CoreUserDTO result = UserCache.getUserByPhone(phone,tenantCode);
                JpowerAssert.notNull(result, JpowerError.Business, "用户不存在");
                return result;
            }
        }
        return null;
    }

}
