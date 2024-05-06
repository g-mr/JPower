package top.jpower.jpower.auth.granter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.common.auth.UserInfo;

import static top.jpower.jpower.auth.granter.OtherCodeTokenGranter.GRANT_TYPE;


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
