package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @Author 郭丁志
 * @Description //TODO 第三方Code登录 默认实现类
 * @Date 00:50 2020-07-28
 **/
@Slf4j
@Component
@AllArgsConstructor
public class OtherCodeTokenGranter implements TokenGranter {
    public static final String GRANT_TYPE = "otherCode";

    protected TbCoreUserDao coreUserDao;

    @Override
    public UserInfo grant(ChainMap tokenParameter) {
        String otherCode = tokenParameter.getStr("otherCode");
        String userType = tokenParameter.getStr("userType");
        UserInfo userInfo = null;
        if (Fc.isNotBlank(otherCode)) {
            //不同的用户类型都可以在这里写逻辑，或者重写这个类的方法
            if(Fc.equals(userType,"web")){
                TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
                        .lambda().eq(TbCoreUser::getOtherCode,otherCode));
                return TokenGranterBuilder.toUserInfo(result);
            }else {
                TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
                        .lambda().eq(TbCoreUser::getOtherCode,otherCode));
                return TokenGranterBuilder.toUserInfo(result);
            }
        }
        return userInfo;
    }

}
