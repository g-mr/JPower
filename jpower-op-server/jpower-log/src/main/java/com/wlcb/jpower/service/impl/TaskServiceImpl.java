package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.handler.HttpInfoHandler;
import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.OkHttp;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mr.g
 * @date 2021-04-02 11:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {


//    public static void main(String[] args) throws IOException {
//        HashMap<String,String> map = ChainMap.newMap();
//        map.put("domain","http:%2F%2Fjpower.top:81");
//
////        HashMap<String,String> map1 = ChainMap.newMap();
////        map1.put("jpower-auth","jpower eyJ0eXBlIjoiSnNvbldlYlRva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJhZGRyZXNzIjoi5YaF6JKZ5Y-kIiwiaWRUeXBlIjoxLCJsb2dpbklkIjoicm9vdCIsIm9yZ05hbWUiOiIiLCJuaWNrTmFtZSI6Iui2hee6p-eUqOaItyIsInRlbGVwaG9uZSI6IjE1MDExMDcxMjI2IiwiYXZhdGFyIjoiNjNlOTNiZWUzNmRiNGM3NGE2ZGEyODMxZmE3NzFhNmZjYjRiYWU3YWE5YjJmYTc5M2M1Mzc5ZDJhZGI4ZmI1MmUxYWRjYmJkM2YwN2RiZDkiLCJ0ZW5hbnRDb2RlIjoiMDAwMDAwIiwidXNlck5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJpZE5vIjoiMTUyNjAxMTk5NDA4MDEyNjE3IiwidXNlcklkIjoiMSIsImxvZ2luQ291bnQiOjE0NTksIm9yZ0lkIjoiOWMzNTZiMWY2MWZjYTIxOTE5YmVhMTVlMWVhOTY0MWIiLCJsYXN0TG9naW5UaW1lIjoxNjE1MDQxNTE2MDAwLCJyb2xlSWRzIjpbIjEiXSwiY2xpZW50Q29kZSI6ImFkbWluIiwicG9zdENvZGUiOiIwMTIwMDAiLCJpc1N5c1VzZXIiOjAsInVzZXJUeXBlIjowLCJlbWFpbCI6IjE2MzQ1NjY2MDZAcXEuY29tIiwiZXhwIjoxNjE3MzkwOTI0LCJuYmYiOjE2MTczODkxMjR9.zL0BFwzK3963TUm5f-dPgc7vVfaZQE14EDjCrTfS-78");
//
//
////        System.out.println(HttpClient.doGet("http://jpower.top:81/api/jpower-system/core/tenant/queryByDomainx",map1,map));
//
//
//        OkHttp okHttp = OkHttp.get("http://localhost/jpower-system/core/tenant/queryByDomain",map).execute(new AuthInterceptor("jpower-auth","jpower eyJ0eXBlIjoiSnNvbldlYlRva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJhZGRyZXNzIjoi5YaF6JKZ5Y-kIiwiaWRUeXBlIjoxLCJsb2dpbklkIjoicm9vdCIsIm9yZ05hbWUiOiIiLCJuaWNrTmFtZSI6Iui2hee6p-eUqOaItyIsInRlbGVwaG9uZSI6IjE1MDExMDcxMjI2IiwiYXZhdGFyIjoiNjNlOTNiZWUzNmRiNGM3NGE2ZGEyODMxZmE3NzFhNmZjYjRiYWU3YWE5YjJmYTc5M2M1Mzc5ZDJhZGI4ZmI1MmUxYWRjYmJkM2YwN2RiZDkiLCJ0ZW5hbnRDb2RlIjoiMDAwMDAwIiwidXNlck5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJpZE5vIjoiMTUyNjAxMTk5NDA4MDEyNjE3IiwidXNlcklkIjoiMSIsImxvZ2luQ291bnQiOjE0NTksIm9yZ0lkIjoiOWMzNTZiMWY2MWZjYTIxOTE5YmVhMTVlMWVhOTY0MWIiLCJsYXN0TG9naW5UaW1lIjoxNjE1MDQxNTE2MDAwLCJyb2xlSWRzIjpbIjEiXSwiY2xpZW50Q29kZSI6ImFkbWluIiwicG9zdENvZGUiOiIwMTIwMDAiLCJpc1N5c1VzZXIiOjAsInVzZXJUeXBlIjowLCJlbWFpbCI6IjE2MzQ1NjY2MDZAcXEuY29tIiwiZXhwIjoxNjE3NDcxMzkzLCJuYmYiOjE2MTc0Njk1OTN9.KO1xcHNNykzi2VjibeqAbfMu59lw6aCdF6eF6xZTxuo"));
//        if (!Fc.isNull(okHttp.getResponse())){
//            System.out.println(okHttp.getResponse().toString());
//            System.out.println(okHttp.getResponse().body().string());
//        }else {
//            System.out.println(okHttp.getError());
//        }
//        okHttp.close();
//    }

    @Override
    public void process(MonitorRestfulProperties.Routes route) {
        String body = OkHttp.get(route.getUrl()+route.getLocation()).execute().getBody();
        if (Fc.isNotBlank(body)){
            JSONObject restFulInfo = JSON.parseObject(body);
            restFulInfo.getJSONObject("paths").forEach((url,methods)->{
                HttpInfoHandler handler = new HttpInfoHandler(JSON.parseObject(Fc.toStr(methods)),restFulInfo.getJSONObject("definitions"));
                handler.getMethodTypes().forEach(method -> {

                    Map<String,String> paths = handler.getPathParam(method);
                    String httpUrl = route.getUrl().concat(StringUtil.format(url,paths));
                    Map<String,String> headers = handler.getHeaderParam(method);
                    Map<String,String> forms = handler.getFormParam(method);
                    Map<String,String> bodys = handler.getBodyParam(method);
                    requestRestFul(httpUrl,method.toUpperCase(),headers,forms,bodys);

                });
            });
        }
    }

    /**
     * 请求接口
     * @author mr.g
     * @param httpUrl 请求地址
     * @param method 请求类型
     * @param headers header参数
     * @param forms form参数
     * @param bodys body参数
     * @return void
     */
    private void requestRestFul(String httpUrl, String method, Map<String,String> headers, Map<String,String> forms, Map<String,String> bodys) {

        AuthInterceptor interceptor = new AuthInterceptor("","");

        switch (method) {
            case "HEAD" :
                OkHttp.head(httpUrl,headers,forms).execute(interceptor);
                break;
            case "GET" :
                OkHttp.get(httpUrl,headers,forms).execute(interceptor);
                break;
            case "PUT" :
            case "POST" :
                if (bodys.size() == 1) {
                    StringBuffer sb = new StringBuffer(httpUrl);
                    sb.append("?clientId=jpower");
                    if (forms != null && forms.keySet().size() > 0) {
                        forms.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
                    }
                    httpUrl = sb.toString();
                }
            default:
                if (bodys.size() == 1) {
                    OkHttp.content(httpUrl,method,headers,bodys.entrySet().iterator().next().getValue(),OkHttp.JSON).execute(interceptor);
                }else {
                    forms.putAll(bodys);
                    OkHttp.method(httpUrl,method,headers,forms).execute(interceptor);
                }
                break;
        }
    }


}
