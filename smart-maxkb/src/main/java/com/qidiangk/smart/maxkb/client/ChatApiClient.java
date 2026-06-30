package com.qidiangk.smart.maxkb.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.qidiangk.smart.maxkb.client.po.client.ChatBO;
import com.qidiangk.smart.maxkb.client.po.client.MessageDO;
import com.qidiangk.smart.maxkb.config.annotation.Return;
import retrofit2.http.*;

/**
 * 聊天
 *
 * @author mr.g
 */
@RetrofitClient(baseUrl = "${maxkb.baseUrl}", readTimeoutMs = 0, path = "chat/api")
public interface ChatApiClient {

    /**
     * 查询智能体下拉列表
     */
    @GET("open")
    @Return
    String chatId(@Header("Authorization") String key);

    /**
     * 聊天
     */
    @POST("chat_message/{chatId}")
    @Return
    MessageDO chatMessage(@Header("Authorization") String key,
                          @Path("chatId") String chatId,
                          @Body ChatBO chatBO);

}
