package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.HttpMethod;
import com.wlcb.jpower.dbs.dao.LogMonitorResultDao;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.handler.AuthBuilder;
import com.wlcb.jpower.handler.HttpInfoBuilder;
import com.wlcb.jpower.handler.HttpInfoHandler;
import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.interceptor.RollbackInterceptor;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JsonUtil;
import com.wlcb.jpower.module.common.utils.OkHttp;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2021-04-02 11:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String PATHS = "paths";
    private static final String DEFINITIONS = "definitions";
    private static final String BASEPATH = "basePath";

    private final LogMonitorResultDao logMonitorResultDao;
    private final RollbackInterceptor rollbackInterceptor;
    private final JpowerProperties properties;
    private AuthInterceptor authInterceptor;

    @Override
    public void process(MonitorRestfulProperties.Route route) {

        log.info("---> START TEST SERVER {} {}",route.getName(),route.getLocation()+route.getUrl());

        authInterceptor = Fc.isNull(route.getAuth())?authInterceptor:AuthBuilder.getInterceptor(route);
        TbLogMonitorResult result = saveResult(route.getName(), route.getUrl(), OkHttp.get(route.getLocation()+route.getUrl()).execute(authInterceptor));

        if (Fc.equals(HttpStatus.SC_OK,result.getResposeCode())){
            JSONObject restFulInfo = JSON.parseObject(result.getRestfulResponse());
            if (restFulInfo.containsKey(PATHS)){
                JSONObject paths = restFulInfo.getJSONObject(PATHS);
                log.info("---> SERVER RESTFUL SUM={}",paths.size());
                paths.forEach((url,methods)->{
                    String httpUrl = route.getLocation().concat(restFulInfo.getString(BASEPATH)).concat(url);
                    HttpInfoHandler handler = HttpInfoBuilder.newHandler(httpUrl,JSON.parseObject(Fc.toStr(methods)),restFulInfo.getJSONObject(DEFINITIONS));
                    handler.getMethodTypes().forEach(method -> {
                        OkHttp okHttp = null;
                        try{

//                            if (url.endsWith("/corporate/export") || url.endsWith("/speaker/training/getText")){
                                log.info("--> START TEST REST {} {}",method,url);
                                okHttp = requestRestFul(method,httpUrl);
                                saveResult(route.getName(),url,okHttp);
//                            }

                        }catch (Exception e){
                            log.error("===>  接口测试异常，error={}",e.getMessage());
                            if (e instanceof BusinessException){
                                throw e;
                            }
                        }finally {
                            if (!Fc.isNull(okHttp)){
                                okHttp.close();
                            }
                            log.info("<-- END TEST REST {} {}",method,url);
                        }
                    });
                });
            }else {
                log.info("  {}服务未发现接口",route.getName());
            }
        }else {
            log.info("  获取服务接口数据异常resposeCode=>{}",result.getResposeCode());
        }
        log.info("<--- END TEST SERVER {} {}; Response={}",route.getName(),route.getLocation()+route.getUrl(),result.getResposeCode());
    }

    /**
     * 保存接口请求结果
     * @Author mr.g
     * @param name 服务名称
     * @param path 监控地址
     * @param okHttp 请求体
     * @return void
     **/
    private TbLogMonitorResult saveResult(String name, String path, OkHttp okHttp) {
        TbLogMonitorResult result = new TbLogMonitorResult();
        try {
            result.setName(name);
            result.setPath(path);
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
            log.error("===>  保存请求结果出错 ==> {}",e.getMessage());
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
        Map<String,String> paths = HttpInfoBuilder.getHandler(url).getPathParam(method);
        Map<String,String> headers = HttpInfoBuilder.getHandler(url).getHeaderParam(method);
        Map<String,String> forms = HttpInfoBuilder.getHandler(url).getFormParam(method);
        Map<String,String> bodys = HttpInfoBuilder.getHandler(url).getBodyParam(method);

        String httpUrl = StringUtil.format(url,paths);
        log.info("  TEST REST URL=>{}",httpUrl);

        OkHttp okHttp;
        switch (method.toUpperCase()) {
            case HttpMethod.HEAD :
                okHttp = OkHttp.head(httpUrl,headers,forms);
                break;
            case HttpMethod.GET :
                okHttp = OkHttp.get(httpUrl,headers,forms);
                break;
            case HttpMethod.DELETE :
            case HttpMethod.PATCH :
            case HttpMethod.OPTIONS :
            case HttpMethod.TRACE :
            case HttpMethod.PUT :
            case HttpMethod.POST :
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
                    if (!AppConstant.PROD_CODE.equals(properties.getEnv())){
                        log.info("  TEST REST PARAMS bodys==>{}",body.getValue());
                    }
                    //获取Body请求数据类型
                    String dataType = HttpInfoBuilder.getHandler(url).getBodyDataType(method);
                    if(StringUtil.startsWithIgnoreCase(dataType, ContentType.APPLICATION_XML.getMimeType())){
                        String clz = HttpInfoBuilder.getHandler(url).getBodyClass(method,body.getKey());
                        okHttp = OkHttp.content(httpUrl,method,headers, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + clz + ">" + JsonUtil.json2xml(body.getValue()) + "</" + clz + ">",OkHttp.XML);
                    }else {
                        okHttp = OkHttp.content(httpUrl,method,headers,body.getValue(),OkHttp.JSON);
                    }
                }else {
                    forms.putAll(bodys);
                    okHttp = OkHttp.method(httpUrl,method,headers,forms);
                }
                break;
        }


        logBefore(okHttp,forms);
        long startNs = System.nanoTime();
        okHttp = okHttp.execute(authInterceptor,rollbackInterceptor);
        logAfter(okHttp,TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));

        return okHttp;
    }

    private void logAfter(OkHttp okHttp,long time){
        if (Fc.isNull(okHttp.getResponse())){
            log.info("  END REQUEST TEST REST FAILED {} : {}"," (" + time + "ms)",okHttp.getError());
        }else {
            ResponseBody responseBody = okHttp.getResponse().body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            log.info("  END REQUEST TEST REST {} {} {}",okHttp.getResponse().code(),(okHttp.getResponse().message().isEmpty() ? "" : ' ' + okHttp.getResponse().message())
                    ," (" + time + "ms" + ", " + bodySize + " body" + ")");
        }
    }

    private void logBefore(OkHttp okHttp,Map<String,String> forms){
        if (!AppConstant.PROD_CODE.equals(properties.getEnv())){
            log.info("  TEST REST PARAMS FORMS==>");
            forms.forEach((key,val)->{
                log.info("    {}: {}",key,val);
            });

            log.info("  TEST REST PARAMS HEADERS==>");
            Headers hd = okHttp.getRequest().headers();
            for (int i = 0, count = hd.size(); i < count; i++) {
                String name = hd.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    log.info("    {}: {}",name,hd.value(i));
                }
            }
        }
        RequestBody requestBody = okHttp.getRequest().body();
        try {
            if (Objects.requireNonNull(requestBody).contentType() != null) {
                log.info("  Content-Type: " + requestBody.contentType());
            }
            if (Objects.requireNonNull(requestBody).contentLength() != -1) {
                log.info("  Content-Length: " + requestBody.contentLength());
            }
        } catch (Exception ignored) {}
    }

}
