package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.dbs.dao.LogMonitorResultDao;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.handler.HttpInfoBuilder;
import com.wlcb.jpower.handler.HttpInfoHandler;
import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.interceptor.RollbackInterceptor;
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
    private RollbackInterceptor rollbackInterceptor;


    @Override
    public void process(MonitorRestfulProperties.Routes route) {
        TbLogMonitorResult result = saveResult(OkHttp.get(route.getLocation()+route.getUrl()).execute());
        if (result.getResposeCode() == 200){
            JSONObject restFulInfo = JSON.parseObject(result.getRestfulResponse());
            restFulInfo.getJSONObject("paths").forEach((url,methods)->{
                String httpUrl = route.getLocation().concat(url);
                HttpInfoHandler handler = HttpInfoBuilder.newHandler(httpUrl,JSON.parseObject(Fc.toStr(methods)),restFulInfo.getJSONObject("definitions"));
                handler.getMethodTypes().forEach(method -> {
                    OkHttp okHttp = requestRestFul(method,httpUrl);
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

            logMonitorResultDao.save(result);
        }catch (Exception e){
            log.error("保存请求结果出错 ==> {}",e.getMessage());
        }finally {
            okHttp.close();
        }
        return result;
    }

    /**
     * 请求接口
     * @author mr.g
     * @param method 请求类型
     * @param url
     * @return void
     */
    private OkHttp requestRestFul(String method, String url) {

        AuthInterceptor interceptor = new AuthInterceptor("admin","123456");

        Map<String,String> paths = HttpInfoBuilder.getHandler(url).getPathParam(method);
        Map<String,String> headers = HttpInfoBuilder.getHandler(url).getHeaderParam(method);
        Map<String,String> forms = HttpInfoBuilder.getHandler(url).getFormParam(method);
        Map<String,String> bodys = HttpInfoBuilder.getHandler(url).getBodyParam(method);

        String httpUrl = StringUtil.format(url,paths);

        OkHttp okHttp;
        switch (method.toUpperCase()) {
            case "HEAD" :
                okHttp = OkHttp.head(httpUrl,headers,forms).execute(interceptor,rollbackInterceptor);
                break;
            case "GET" :
                okHttp = OkHttp.get(httpUrl,headers,forms).execute(interceptor,rollbackInterceptor);
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
                    String dataType = HttpInfoBuilder.getHandler(url).getBodyDataType(method);
                    if(StringUtil.startsWithIgnoreCase(dataType, "application/xml")){
                        String clz = HttpInfoBuilder.getHandler(url).getBodyClass(method,body.getKey());
                        okHttp = OkHttp.content(httpUrl,method,headers, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + clz + ">" + JsonUtil.json2xml(body.getValue()) + "</" + clz + ">",OkHttp.XML).execute(interceptor);
                    }else {
                        okHttp = OkHttp.content(httpUrl,method,headers,body.getValue(),OkHttp.JSON).execute(interceptor,rollbackInterceptor);
                    }
                }else {
                    forms.putAll(bodys);
                    okHttp = OkHttp.method(httpUrl,method,headers,forms).execute(interceptor,rollbackInterceptor);
                }
                break;
        }

        return okHttp;
    }


}
