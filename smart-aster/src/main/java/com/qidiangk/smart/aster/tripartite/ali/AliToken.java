package com.qidiangk.smart.aster.tripartite.ali;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.nls.client.AccessToken;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.redis.cache.RedisService;
import com.qidiangk.smart.aster.tripartite.property.AliProperty;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AliToken  {

    private static final RedisService REDIS_SERVICE = SpringUtil.getBean(RedisService.class);
    private static final String TOKEN_KEY = "ivr_token:";

    protected AliProperty aliProperty;

    protected AliToken(AliProperty aliProperty) {
        this.aliProperty = aliProperty;
    }

    protected String getToken(){
        log.info("初始化阿里语音合成===>> {} , {}", aliProperty.getAccessKeyId(), aliProperty.getAccessKeySecret());
        String key = TOKEN_KEY + aliProperty.getAccessKeyId();

        String token = REDIS_SERVICE.valueOps(String.class).get(key);
        if (StrUtil.isNotBlank(token)){
            return token;
        }

        AccessToken accessToken = new AccessToken(aliProperty.getAccessKeyId(), aliProperty.getAccessKeySecret());
        try {
            accessToken.apply();
            log.info("get token: {}, expire time: {}", accessToken.getToken(), accessToken.getExpireTime());

            long timeout = DateUtil.between(DateUtil.date(), DateUtil.date(accessToken.getExpireTime()*1000), DateUnit.SECOND);
            REDIS_SERVICE.valueOps().set(key, accessToken.getToken(), timeout-3, TimeUnit.SECONDS);

        } catch (IOException e) {
            log.error("阿里云报错", e);
        }
        return accessToken.getToken();
    }

}
