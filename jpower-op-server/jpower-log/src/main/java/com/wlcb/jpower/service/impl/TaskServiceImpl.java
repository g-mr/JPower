package com.wlcb.jpower.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.HttpMethod;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wlcb.jpower.dbs.dao.LogMonitorResultDao;
import com.wlcb.jpower.dbs.entity.TbLogMonitorParam;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
import com.wlcb.jpower.handler.AuthBuilder;
import com.wlcb.jpower.handler.HttpInfoBuilder;
import com.wlcb.jpower.handler.HttpInfoHandler;
import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.interceptor.LogInterceptor;
import com.wlcb.jpower.interceptor.RollbackInterceptor;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.properties.MonitorRestfulProperties.Route;
import com.wlcb.jpower.service.MonitorSettingService;
import com.wlcb.jpower.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author mr.g
 * @date 2021-04-02 11:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String PATHS = "paths";
    private static final String TAGS = "tags";
    private static final String DEFINITIONS = "definitions";
    private static final String BASEPATH = "basePath";

    private final LogMonitorResultDao logMonitorResultDao;
    private final MonitorSettingService monitorSettingService;
    private final RollbackInterceptor rollbackInterceptor;
    private AuthInterceptor authInterceptor;
    private final LogInterceptor logInterceptor;

    private AuthInterceptor getAuthInterceptor(Route route){
        return Fc.isNull(route.getAuth())?authInterceptor:AuthBuilder.getInterceptor(route);
    }

    private static class RestCache{
        private static final Cache<String, JSONObject> cache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.DAYS) // 设置缓存在写入一天后失效
                .concurrencyLevel(Runtime.getRuntime().availableProcessors()) // 设置并发级别为cpu核心数，默认为4
                .recordStats() // 开启缓存统计
                .build();

        public static void set(String key, JSONObject value){
            cache.put(key,value);
        }

        public static JSONObject get(String key){
            return cache.getIfPresent(key);
        }


        public static JSONObject getOrDefault(Route route) {
            try {
                return cache.get(route.getName(), () -> {
                    String content = OkHttp.get(route.getLocation()+route.getUrl())
                            .execute(Fc.isNull(route.getAuth())?SpringUtil.getBean(AuthInterceptor.class):AuthBuilder.getInterceptor(route))
                            .getBody();
                    log.debug("{}获取到接口信息完成:{}",route.getName(),content);
                    return JSON.parseObject(Fc.isNotBlank(content)&&JsonUtil.isJsonObject(content)?content:"{}");
                });
            } catch (Exception e) {
                if (Fc.isNull(route)){
                    log.error("获取全部接口出错，route is null");
                }else {
                    e.printStackTrace();
                    log.error("获取全部接口出错，name={},host={},error={}",route.getName(),route.getLocation(),e.getMessage());
                }
                return new JSONObject();
            }
        }
    }

    @Override
    public void process(Route route) {

        log.debug("---> START TEST SERVER {} {}",route.getName(),route.getLocation()+route.getUrl());

        authInterceptor = getAuthInterceptor(route);
        TbLogMonitorResult result = saveResult(route.getName(), route.getUrl(), OkHttp.get(route.getLocation()+route.getUrl()).execute(authInterceptor),new TbLogMonitorSetting());

        if (Fc.equals(HttpStatus.SC_OK,result.getResposeCode())){
            JSONObject restFulInfo = JSON.parseObject(result.getRestfulResponse());

            if (restFulInfo.containsKey(PATHS)){
                JSONObject paths = restFulInfo.getJSONObject(PATHS);

                //这里异步执行去和数据库比对，把已经不存在配置删除掉
                ThreadUtil.execAsync(() -> monitorSettingService.deleteSetting(route.getName(),paths.getInnerMap(),restFulInfo.getJSONArray(TAGS)));

                RestCache.set(route.getName(),restFulInfo);
                log.info("---> [{}] SERVER RESTFUL SUM={}",  route.getName(), paths.size());
                paths.forEach((url,methods)->{

                    String httpUrl = route.getLocation().concat(Fc.equals(StringPool.SLASH,restFulInfo.getString(BASEPATH))?StringPool.EMPTY:restFulInfo.getString(BASEPATH)).concat(url);

                    List<TbLogMonitorParam> paramList = monitorSettingService.queryParamByPath(route.getName(),url);
                    HttpInfoHandler handler = HttpInfoBuilder.newHandler(httpUrl,paramList,JSON.parseObject(Fc.toStr(methods)),restFulInfo.getJSONObject(DEFINITIONS));
                    handler.getMethodTypes().forEach(method -> {

                        TbLogMonitorSetting setting = monitorSettingService.getSetting(route.getName(),handler.getTags(method),url,method);

                        if (Fc.equals(setting.getIsMonitor(), ConstantsEnum.YN01.Y.getValue())){
                            OkHttp okHttp = null;
                            try{
                                log.info("--> START TEST REST {} {}",method,url);

                                okHttp = requestRestFul(method,httpUrl);
                                saveResult(route.getName(),url,okHttp,setting);
                            }catch (Exception e){
                                log.error("  接口测试异常，error={}",e.getMessage());
                                if (e instanceof BusinessException){
                                    throw e;
                                }
                            }finally {
                                if (Fc.notNull(okHttp)){
                                    okHttp.close();
                                }
                                log.info("<-- END TEST REST {} {}",method,url);
                            }
                        }
                    });
                });
            }else {
                log.warn("  {}服务未发现接口",route.getName());
            }
        }else {
            log.warn("  获取服务接口数据异常resposeCode=>{}",result.getResposeCode());
        }
        log.debug("<--- END TEST SERVER {} {}; Response={}",route.getName(),route.getLocation()+route.getUrl(),result.getResposeCode());
    }

    /**
     * 保存接口请求结果
     * @Author mr.g
     * @param name 服务名称
     * @param path 监控地址
     * @param okHttp 请求体
     * @param setting
     * @return void
     **/
    public TbLogMonitorResult saveResult(String name, String path, OkHttp okHttp, TbLogMonitorSetting setting) {
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
                result.setIsSuccess(ConstantsEnum.YN01.N.getValue());
            }else {
                result.setResponseTime(okHttp.getResponseTime());
                result.setRespose(okHttp.getResponse().toString());
                result.setResposeCode(okHttp.getResponse().code());
                result.setRestfulResponse(okHttp.getBody());

                int isSuccess = ConstantsEnum.YN01.N.getValue();

                if (Fc.notNull(setting.getCode())){
                    isSuccess = setting.getCode().contains(Fc.toStr(okHttp.getResponse().code()))?ConstantsEnum.YN01.Y.getValue():ConstantsEnum.YN01.N.getValue();
                }

                if (isSuccess==ConstantsEnum.YN01.Y.getValue() && Fc.notNull(setting.getExecJs())){
                    try{
                        boolean is = JsUtil.execJsFunction("function exc(result){"+setting.getExecJs()+"}","exc",result.getRestfulResponse());
                        isSuccess = is?ConstantsEnum.YN01.Y.getValue():ConstantsEnum.YN01.N.getValue();
                    }catch (Exception e){
                        isSuccess = ConstantsEnum.YN01.N.getValue();
                    }
                }

                if (Fc.isNull(setting.getCode()) && Fc.isNull(setting.getExecJs())){
                    isSuccess = okHttp.getResponse().isSuccessful()?ConstantsEnum.YN01.Y.getValue():ConstantsEnum.YN01.N.getValue();
                }
                result.setIsSuccess(isSuccess);
            }

            logMonitorResultDao.save(result);
        }catch (Exception e){
            log.error("===>  保存请求结果出错 ==> {}",e.getMessage());
            e.printStackTrace();
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
        Map<String,String> headers = HttpInfoBuilder.getHandler(url).getHeaderParam(method, true);
        Map<String,String> forms = HttpInfoBuilder.getHandler(url).getFormParam(method,true);
        Map<String,String> bodys = HttpInfoBuilder.getHandler(url).getBodyParam(method);

        String httpUrl = StringUtil.format(url,paths);

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
                    //获取Body请求数据类型
                    String dataType = HttpInfoBuilder.getHandler(url).getBodyDataType(method);
                    if(StringUtil.startWithIgnoreCase(dataType, ContentType.APPLICATION_XML.getMimeType())){
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


        okHttp = okHttp.execute(authInterceptor,rollbackInterceptor,logInterceptor);
        return okHttp;
    }

    @Override
    public JSONArray tagList(Route route) {
        JSONObject json = RestCache.getOrDefault(route);
        if (json.containsKey(TAGS)){
            return json.getJSONArray(TAGS);
        }
        return new JSONArray();
    }

    @Override
    public JSONArray tree(Route route) {
        JSONArray array = new JSONArray();

        JSONObject json = RestCache.getOrDefault(route);
        if (json.containsKey(TAGS)){
            json.getJSONArray(TAGS).forEach(tag -> {
                JSONObject tagJson = new JSONObject();
                tagJson.put("name",((JSONObject)tag).getString("name"));
                if (json.containsKey(PATHS)){
                    JSONArray jsonArray = getTagChildren(json.getJSONObject(PATHS),tagJson.getString("name"));
                    if (jsonArray.size() > 0){
                        tagJson.put("children",jsonArray);
                    }
                }
                array.add(tagJson);
            });
        }
        return array;
    }

    /**
     * 获取一个paths下的子级
     * @Author mr.g
     * @param paths
     * @param tag
     * @return com.alibaba.fastjson.JSONArray
     **/
    private JSONArray getTagChildren(Map<String, Object> paths, String tag) {
        JSONArray array = new JSONArray();
        for (Map.Entry<String, Object> entry : paths.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            List<JSONObject> list = ((Map<String, Object>) value).entrySet().stream().filter(e -> {
                JSONObject json = (JSONObject) e.getValue();
                return json.getJSONArray(TAGS).contains(tag);
            }).map(e -> {
                JSONObject json = new JSONObject();
                json.put("name", e.getKey());
                return json;
            }).collect(Collectors.toList());

            if (list.size() > 0) {
                JSONObject js = new JSONObject();
                js.put("name", key);
                js.put("children", list);
                array.add(js);
            }
        }
        return array;
    }

    @Override
    public List<Map<String, Object>> getParams(Route route, String path, String method) {
        List<Map<String, Object>> list = new ArrayList<>();

        JSONObject json = RestCache.getOrDefault(route);
        if (json.containsKey(PATHS)){
            JSONObject paths = json.getJSONObject(PATHS);
            JSONObject methodsInfo = paths.getJSONObject(path);
            HttpInfoHandler handler = new HttpInfoHandler(monitorSettingService.queryParamByPath(route.getName(),path),methodsInfo,json.getJSONObject(DEFINITIONS));

            handler.getPathParam(method).forEach((key,val) -> list.add(ChainMap.init().set("name",key).set("value",val).set("type","path")));
            handler.getHeaderParam(method, false).forEach((key,val) -> list.add(ChainMap.init().set("name",key).set("value",val).set("type","header")));
            handler.getFormParam(method,false).forEach((key,val) -> list.add(ChainMap.init().set("name",key).set("value",val).set("type","query")));
            handler.getBodyParam(method).forEach((key,val) -> list.add(ChainMap.init().set("name",key).set("value",val).set("type","body")));
        }
        return list;
    }
}
