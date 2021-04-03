package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.OkHttp;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author mr.g
 * @date 2021-04-02 11:27
 */
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

//    @Autowired
//    public TaskServiceImpl(Decoder decoder, Encoder encoder) {
//        feignClient = Feign.builder()
//                //.client(client)
//                .encoder(encoder)
//                .decoder(decoder)
//                .target(Target.EmptyTarget.create(DynamicFeignClient.class));
//    }

    public static void main(String[] args) throws IOException {
        HashMap<String,String> map = ChainMap.newMap();
        map.put("domain","http:%2F%2Fjpower.top:81");

//        HashMap<String,String> map1 = ChainMap.newMap();
//        map1.put("jpower-auth","jpower eyJ0eXBlIjoiSnNvbldlYlRva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJhZGRyZXNzIjoi5YaF6JKZ5Y-kIiwiaWRUeXBlIjoxLCJsb2dpbklkIjoicm9vdCIsIm9yZ05hbWUiOiIiLCJuaWNrTmFtZSI6Iui2hee6p-eUqOaItyIsInRlbGVwaG9uZSI6IjE1MDExMDcxMjI2IiwiYXZhdGFyIjoiNjNlOTNiZWUzNmRiNGM3NGE2ZGEyODMxZmE3NzFhNmZjYjRiYWU3YWE5YjJmYTc5M2M1Mzc5ZDJhZGI4ZmI1MmUxYWRjYmJkM2YwN2RiZDkiLCJ0ZW5hbnRDb2RlIjoiMDAwMDAwIiwidXNlck5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJpZE5vIjoiMTUyNjAxMTk5NDA4MDEyNjE3IiwidXNlcklkIjoiMSIsImxvZ2luQ291bnQiOjE0NTksIm9yZ0lkIjoiOWMzNTZiMWY2MWZjYTIxOTE5YmVhMTVlMWVhOTY0MWIiLCJsYXN0TG9naW5UaW1lIjoxNjE1MDQxNTE2MDAwLCJyb2xlSWRzIjpbIjEiXSwiY2xpZW50Q29kZSI6ImFkbWluIiwicG9zdENvZGUiOiIwMTIwMDAiLCJpc1N5c1VzZXIiOjAsInVzZXJUeXBlIjowLCJlbWFpbCI6IjE2MzQ1NjY2MDZAcXEuY29tIiwiZXhwIjoxNjE3MzkwOTI0LCJuYmYiOjE2MTczODkxMjR9.zL0BFwzK3963TUm5f-dPgc7vVfaZQE14EDjCrTfS-78");


//        System.out.println(HttpClient.doGet("http://jpower.top:81/api/jpower-system/core/tenant/queryByDomainx",map1,map));


        //实现Authenticator接口，试试是否能实现用户名密码操作

        OkHttp okHttp = OkHttp.get("http://localhost/jpower-system/core/tenant/queryByDomain",map).execute(new AuthInterceptor("jpower-auth","jpower eyJ0eXBlIjoiSnNvbldlYlRva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJhZGRyZXNzIjoi5YaF6JKZ5Y-kIiwiaWRUeXBlIjoxLCJsb2dpbklkIjoicm9vdCIsIm9yZ05hbWUiOiIiLCJuaWNrTmFtZSI6Iui2hee6p-eUqOaItyIsInRlbGVwaG9uZSI6IjE1MDExMDcxMjI2IiwiYXZhdGFyIjoiNjNlOTNiZWUzNmRiNGM3NGE2ZGEyODMxZmE3NzFhNmZjYjRiYWU3YWE5YjJmYTc5M2M1Mzc5ZDJhZGI4ZmI1MmUxYWRjYmJkM2YwN2RiZDkiLCJ0ZW5hbnRDb2RlIjoiMDAwMDAwIiwidXNlck5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJpZE5vIjoiMTUyNjAxMTk5NDA4MDEyNjE3IiwidXNlcklkIjoiMSIsImxvZ2luQ291bnQiOjE0NTksIm9yZ0lkIjoiOWMzNTZiMWY2MWZjYTIxOTE5YmVhMTVlMWVhOTY0MWIiLCJsYXN0TG9naW5UaW1lIjoxNjE1MDQxNTE2MDAwLCJyb2xlSWRzIjpbIjEiXSwiY2xpZW50Q29kZSI6ImFkbWluIiwicG9zdENvZGUiOiIwMTIwMDAiLCJpc1N5c1VzZXIiOjAsInVzZXJUeXBlIjowLCJlbWFpbCI6IjE2MzQ1NjY2MDZAcXEuY29tIiwiZXhwIjoxNjE3NDcxMzkzLCJuYmYiOjE2MTc0Njk1OTN9.KO1xcHNNykzi2VjibeqAbfMu59lw6aCdF6eF6xZTxuo"));
        if (!Fc.isNull(okHttp.getResponse())){
            System.out.println(okHttp.getResponse().toString());
            System.out.println(okHttp.getResponse().body().string());
        }else {
            System.out.println(okHttp.getError());
        }
        okHttp.close();
    }

    @Override
    @SneakyThrows
    public void process(MonitorRestfulProperties.Routes route) {


//        if (!StringUtil.startsWithIgnoreCase(route.getUrl(),"http")){
//            uri = "http://"+route.getUrl();
//        }
//
//        Object o = feignClient.get(URI.create(uri+route.getLocation()), ChainMap.newMap());
//        System.out.println(o);
    }
}