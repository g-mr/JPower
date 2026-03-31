package top.jpower.auth.auth.granter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.util.utils.Fc;
import top.jpower.auth.auth.AuthUserInfo;
import top.jpower.auth.auth.TokenGranter;
import top.jpower.auth.dto.TokenParameter;
import top.jpower.user.api.dto.CoreUserDTO;

import static top.jpower.auth.auth.granter.OtherCodeTokenGranter.GRANT_TYPE;


/**
 * 第三方Code登录
 *
 * @author mr.g
 **/
@Slf4j
@Component(GRANT_TYPE)
public class OtherCodeTokenGranter implements TokenGranter {
    public static final String GRANT_TYPE = "otherCode";

    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

    @Override
    public CoreUserDTO grant(TokenParameter tokenParameter) {
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
