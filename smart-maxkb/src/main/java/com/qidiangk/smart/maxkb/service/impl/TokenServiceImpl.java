package com.qidiangk.smart.maxkb.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.qidiangk.smart.maxkb.client.TokenClient;
import com.qidiangk.smart.maxkb.client.po.TokenBO;
import com.qidiangk.smart.maxkb.client.po.TokenPO;
import com.qidiangk.smart.maxkb.config.property.MaxKBProperty;
import com.qidiangk.smart.maxkb.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.JsonUtil;

import java.time.Duration;

import static top.jpower.core.util.constants.ReturnConstants.RECODE_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private static final String MAXKB_TOKEN = "maxkb:token";
    private static final Duration DURATION = Duration.ofHours(3);

    private final RedissonClient redissonClient;
    private final TokenClient tokenClient;
    private final MaxKBProperty maxKBProperty;

    private TokenBO getReq(){
        return TokenBO.builder().username(maxKBProperty.getUsername()).password(maxKBProperty.getPassword()).build();
    }

    @Override
    public void removeToken() {
        redissonClient.getBucket(MAXKB_TOKEN).delete();
    }

    @Override
    public String getToken() {
        return getToken(false);
    }

    @Override
    public String getToken(boolean verification) {
        RBucket<String> bucket = redissonClient.getBucket(MAXKB_TOKEN);

        if (!bucket.isExists()){
            Response rsp = getToken(bucket);
            if (rsp != null) {
                JpowerAssert.createException(JpowerError.Rpc, rsp.code(), rsp.message());
            }
        }

        if (verification) {
            HttpResponse response = HttpRequest.get(StrUtil.removeSuffix(maxKBProperty.getBaseUrl(), "/") + "/admin/api/user/profile").header("Authorization", "Bearer " + bucket.get()).execute();
            JSONObject json = JSONUtil.parseObj(response.body());
            if (!response.isOk()
                    || Fc.equalsValue(json.getStr("code"), "1002")
                    || Fc.equalsValue(json.getStr("code"), "1003")) {
                removeToken();
                getToken(bucket);
            }
        }

        return bucket.get();
    }

    @Override
    public synchronized Response getToken(RBucket<String> bucket){
        retrofit2.Response<R<TokenPO>> rp = tokenClient.refresh(getReq());

        if (rp.isSuccessful() && Fc.equalsValue(rp.body().getCode(), RECODE_SUCCESS)){
            bucket.set(rp.body().getData().getToken(), DURATION);
            return null;
        }
        return rp.raw();
    }

}
