package com.qidiangk.smart.maxkb.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.qidiangk.smart.maxkb.client.po.PageVO;
import com.qidiangk.smart.maxkb.client.po.agent.ApplicationKeyDO;
import com.qidiangk.smart.maxkb.config.annotation.Return;
import com.qidiangk.smart.maxkb.config.annotation.Token;
import com.qidiangk.smart.maxkb.pojo.vo.SelectVO;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

@RetrofitClient(baseUrl = "${maxkb.baseUrl}", path = "admin/api")
@Token
public interface AgentClient {

    /**
     * 查询智能体下拉列表
     */
    @GET("workspace/default/application")
    @Return
    List<SelectVO> select(@QueryMap Map<String, Object> search);

    /**
     * 查询智能体密钥列表
     * <br />
     * 根据过期时间倒序排序
     *
     * @param applicationId 智能体应用ID
     */
    @GET("workspace/default/application/{applicationId}/application_key/1/100?order_by=-expire_time")
    @Return
    PageVO<ApplicationKeyDO> apiKeyList(@Path("applicationId") String applicationId);

    /**
     * 创建智能体密钥
     *
     * @param applicationId 智能体应用ID
     */
    @POST("workspace/default/application/{applicationId}/application_key")
    @Return
    ApplicationKeyDO createApiKey(@Path("applicationId") String applicationId);

}
