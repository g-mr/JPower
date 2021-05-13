package com.wlcb.jpower.auth;

import com.wlcb.jpower.auth.granter.PasswordTokenGranter;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.utils.Fc;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TokenGranterBuilder
 * @Description TODO 构造登录查询
 * @Author 郭丁志
 * @Date 2020-07-28 00:34
 * @Version 1.0
 */
@Component
public class TokenGranterBuilder {

    /**
     * TokenGranter缓存池
     */
    private Map<String, TokenGranter> granterPool = new ConcurrentHashMap<>();

    public TokenGranterBuilder(Map<String, TokenGranter> granterPool) {
        granterPool.forEach(this.granterPool::put);
    }

    /**
     * 获取TokenGranter
     *
     * @param grantType 授权类型
     * @return TokenGranter
     */
    public TokenGranter getGranter(String grantType) {
        TokenGranter tokenGranter = granterPool.get(Fc.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
        if (tokenGranter == null) {
            throw new BusinessException("grantType无效，请传递正确的grantType参数");
        } else {
            return tokenGranter;
        }
    }

}
