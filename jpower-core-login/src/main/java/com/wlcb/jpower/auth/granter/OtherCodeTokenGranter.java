package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.feign.UserClient;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author 郭丁志
 * @Description //TODO 第三方Code登录 默认实现类
 * @Date 00:50 2020-07-28
 **/
@Slf4j
@Component
public class OtherCodeTokenGranter implements TokenGranter {
    public static final String GRANT_TYPE = "otherCode";

    @Resource
    protected UserClient userClient;
    @Autowired(required = false)
    private AuthUserInfo authUserInfo;

    @Override
    public UserInfo grant(ChainMap tokenParameter) {
        String otherCode = tokenParameter.getStr("otherCode");
        if (Fc.isNotBlank(otherCode)) {

            if (!Fc.isNull(authUserInfo)){
                return authUserInfo.getOtherCodeUserInfo(tokenParameter);
            }else {
                TbCoreUser result = userClient.queryUserByCode(otherCode).getData();
                return TokenGranterBuilder.toUserInfo(result);
            }

        }
        return null;
    }

}
