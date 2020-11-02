package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author 郭丁志
 * @Description //TODO 第三方Code登录 默认实现类
 * @Date 00:50 2020-07-28
 **/
@Slf4j
@Component
public class OtherCodeTokenGranter implements TokenGranter {
    public static final String GRANT_TYPE = "otherCode";

    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

    @Override
    public UserInfo grant(ChainMap tokenParameter) {
        String otherCode = tokenParameter.getStr("otherCode");
        String tenantCode = tokenParameter.getStr("tenantCode");
        if (Fc.isNotBlank(otherCode)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getOtherCodeUserInfo(tokenParameter);
            }else {
//                TbCoreUser result = UserCache.getUserByCode(otherCode,tenantCode);
//                return TokenGranterBuilder.toUserInfo(result);
                throw new BusinessException("暂不支持第三方验证码登录");
            }

        }
        return null;
    }

}
