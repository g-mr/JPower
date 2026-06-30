package com.qidiangk.smart.maxkb.service;

import okhttp3.Response;
import org.redisson.api.RBucket;

public interface ITokenService {

    void removeToken();

    String getToken();

    String getToken(boolean verification);

    Response getToken(RBucket<String> bucket);

}
