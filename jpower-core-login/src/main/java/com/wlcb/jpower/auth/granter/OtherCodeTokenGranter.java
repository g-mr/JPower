package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.AuthUserInfo;
import com.wlcb.jpower.auth.TokenGranter;
import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wlcb.jpower.auth.granter.OtherCodeTokenGranter.GRANT_TYPE;


/**
 * @Author 郭丁志
 * @Description //TODO 第三方Code登录 默认实现类
 * @Date 00:50 2020-07-28
 **/
@Slf4j
@Component(GRANT_TYPE)
public class OtherCodeTokenGranter implements TokenGranter {
    public static final String GRANT_TYPE = "otherCode";

    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        String otherCode = tokenParameter.getOtherCode();
        String tenantCode = tokenParameter.getTenantCode();
        if (Fc.isNotBlank(otherCode)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getOtherCodeUserInfo(tokenParameter);
            }else {
//                TbCoreUser result = UserCache.getUserByCode(otherCode,tenantCode);
//                return AuthUtil.toUserInfo(result);
                throw new BusinessException("暂不支持第三方验证码登录");
            }

        }
        return null;
    }

}
