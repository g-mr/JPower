package com.qidiangk.smart.auth.auth.granter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.auth.auth.AuthUserInfo;
import com.qidiangk.smart.auth.auth.TokenGranter;
import com.qidiangk.smart.auth.dto.TokenParameter;
import com.qidiangk.smart.auth.utils.TokenUtil;
import com.qidiangk.smart.resource.api.dto.ValidateDTO;
import com.qidiangk.smart.resource.api.feign.SmsClient;
import com.qidiangk.smart.user.api.cache.UserCache;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

import static top.jpower.core.util.constants.JpowerConstants.VALIDATE_SMS_CODE;
import static com.qidiangk.smart.auth.auth.granter.PhoneTokenGranter.GRANT_TYPE;


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
