package com.qidiangk.smart.maxkb.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.qidiangk.smart.maxkb.client.po.TokenBO;
import com.qidiangk.smart.maxkb.client.po.TokenPO;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import top.jpower.core.util.rsp.R;

/**
 * MaxKB登录
 *
 * @author mr.g
 */
@RetrofitClient(baseUrl = "${maxkb.baseUrl}", path = "admin/api")
public interface TokenClient {

    /**
     * 获取Token
     */
    @POST("user/login")
    Response<R<TokenPO>> refresh(@Body TokenBO req);

}
