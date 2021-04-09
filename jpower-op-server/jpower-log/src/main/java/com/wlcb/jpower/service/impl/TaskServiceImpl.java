package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.dbs.dao.LogMonitorResultDao;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.handler.HttpInfoHandler;
import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JsonUtil;
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


    private LogMonitorResultDao logMonitorResultDao;

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
        TbLogMonitorResult result = saveResult(OkHttp.get(route.getUrl()+route.getLocation()).execute());
        if (result.getResposeCode() == 200){
            JSONObject restFulInfo = JSON.parseObject(result.getRestfulResponse());
            restFulInfo.getJSONObject("paths").forEach((url,methods)->{
                HttpInfoHandler handler = new HttpInfoHandler(JSON.parseObject(Fc.toStr(methods)),restFulInfo.getJSONObject("definitions"));
                handler.getMethodTypes().forEach(method -> {

                    Map<String,String> paths = handler.getPathParam(method);
                    String httpUrl = route.getUrl().concat(StringUtil.format(url,paths));

                    OkHttp okHttp = requestRestFul(handler,method,httpUrl);
                    saveResult(okHttp);

                });
            });
        }
    }

    /**
     * 保存接口请求结果
     * @Author mr.g
     * @param okHttp
     * @return void
     **/
    private TbLogMonitorResult saveResult(OkHttp okHttp) {
        TbLogMonitorResult result = new TbLogMonitorResult();
        try {
            result.setUrl(okHttp.getRequest().url().toString());
            result.setMethod(okHttp.getRequest().method());
            result.setHeader(okHttp.getRequest().headers().toString());

            result.setBody(okHttp.getRequestBody());

            if (Fc.isNull(okHttp.getResponse())){
                result.setError(okHttp.getError());
            }else {
                result.setRespose(okHttp.getResponse().toString());
                result.setResposeCode(okHttp.getResponse().code());
                result.setRestfulResponse(okHttp.getBody());
            }

            //如果是上传文件接口怎么办？需要测试一下
            System.out.println(result);
            System.out.println("=========================================");
//            logMonitorResultDao.save(result);
        }catch (Exception e){
            log.error("保存请求结果出错 ==> {}",e.getMessage());
        }
        return result;
    }

    /**
     * 请求接口
     * @author mr.g
     * @param method 请求类型
     * @param httpUrl
     * @return void
     */
    private OkHttp requestRestFul(HttpInfoHandler handler, String method, String httpUrl) {

        AuthInterceptor interceptor = new AuthInterceptor("admin","123456");

        Map<String,String> headers = handler.getHeaderParam(method);
        Map<String,String> forms = handler.getFormParam(method);
        Map<String,String> bodys = handler.getBodyParam(method);

        OkHttp okHttp;
        switch (method.toUpperCase()) {
            case "HEAD" :
                okHttp = OkHttp.head(httpUrl,headers,forms).execute(interceptor);
                break;
            case "GET" :
                okHttp = OkHttp.get(httpUrl,headers,forms).execute(interceptor);
                break;
            case "DELETE" :
            case "PATCH" :
            case "OPTIONS" :
            case "TRACE" :
            case "PUT" :
            case "POST" :
                if (bodys.size() == 1) {
                    StringBuffer sb = new StringBuffer();
                    if (forms != null && forms.keySet().size() > 0) {
                        forms.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
                    }
                    if (sb.length()>0){
                        httpUrl = httpUrl+"?"+StringUtil.removeAllPrefix(sb,"&");
                    }
                }
            default:
                if (bodys.size() == 1) {
                    Map.Entry<String, String> body = bodys.entrySet().iterator().next();
                    //获取Body请求数据类型
                    String dataType = handler.getBodyDataType(method);
                    if(StringUtil.startsWithIgnoreCase(dataType, "application/xml")){
                        String clz = handler.getBodyClass(method,body.getKey());
                        okHttp = OkHttp.content(httpUrl,method,headers, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + clz + ">" + JsonUtil.json2xml(body.getValue()) + "</" + clz + ">",OkHttp.XML).execute(interceptor);
                    }else {
                        okHttp = OkHttp.content(httpUrl,method,headers,body.getValue(),OkHttp.JSON).execute(interceptor);
                    }
                }else {
                    forms.putAll(bodys);
                    okHttp = OkHttp.method(httpUrl,method,headers,forms).execute(interceptor);
                }
                break;
        }

        return okHttp;
    }


}
