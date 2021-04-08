package com.wlcb.jpower.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 22:03
 */
@AllArgsConstructor
public class HttpInfoHandler {

    private static final String STRING_DEFAULT_VAL = "test";
    private static final String NUMBER_DEFAULT_VAL = "0";


    private JSONObject methodsInfo;
    private JSONObject definitions;

    private interface JSON_CONSTANT_KEY {

        String PARAMETERS		= "parameters";

        String IN		        = "in";
        String NAME		        = "name";
        String DEFAULT	        = "default";
        String TYPE	            = "type";
        String REQUIRED         = "required";

        String SCHEMA	        = "schema";
        String ORIGINAL_REF     = "originalRef";
        String $REF             = "$ref";
        String FORMAT	        = "format";
        String PROPERTIES       = "properties";
        String EXAMPLE          = "example";

    }

    private interface JSON_CONSTANT_VALUE {
        String PATH		        = "path";
        String HEADER		    = "header";
        String BODY		        = "body";
        String QUERY		        = "query";
        String STRING		    = "string";
        String DATE		        = "date";
        String DATE_TIME		= "date-time";
        String TIME		        = "time";
        String BOOLEAN		    = "boolean";
        String NUMBER		    = "number";
        String INTEGER		    = "integer";
    }

    /**
     * 获取请求类型
     * @Author mr.g
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getMethodTypes() {
        List<String> list = new ArrayList<>();
        Iterator<String> keys = methodsInfo.keySet().iterator();
        while (keys.hasNext()) {
            list.add(keys.next());
        }
        return list;
    }

    /**
     * 获取path参数
     * @Author mr.g
     * @Date 17:01 2021-04-06
     * @param method 请求类型
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public Map<String,String> getPathParam(String method) {
        Map<String,String> map = ChainMap.newMap();
        methodsInfo.getJSONObject(method).getJSONArray(JSON_CONSTANT_KEY.PARAMETERS).forEach(str -> {
            JSONObject param = (JSONObject) str;
            if (Fc.equals(param.getString(JSON_CONSTANT_KEY.IN),JSON_CONSTANT_VALUE.PATH)){
                map.put(param.getString(JSON_CONSTANT_KEY.NAME),defaultValue(param));
            }
        });

        return map;
    }

    /**
     * 获取header参数
     * @Author mr.g
     * @param method
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public Map<String,String> getHeaderParam(String method) {
        Map<String,String> map = ChainMap.newMap();
        methodsInfo.getJSONObject(method).getJSONArray(JSON_CONSTANT_KEY.PARAMETERS).forEach(str -> {
            JSONObject param = (JSONObject) str;
            if (param.getBoolean(JSON_CONSTANT_KEY.REQUIRED) && Fc.equals(param.getString(JSON_CONSTANT_KEY.IN),JSON_CONSTANT_VALUE.HEADER)){
                map.put(param.getString(JSON_CONSTANT_KEY.NAME),defaultValue(param));
            }
        });
        return map;
    }

    /**
     * 获取Body参数
     * @author mr.g
     * @param method 
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    public Map<String,String> getBodyParam(String method) {
        Map<String,String> map = ChainMap.newMap();
        JSONArray json = methodsInfo.getJSONObject(method).getJSONArray(JSON_CONSTANT_KEY.PARAMETERS);
        if (isMultipleBody(json)){
            return map;
        }

        json.forEach(str -> {
            JSONObject param = (JSONObject) str;
            if (Fc.equals(param.getString(JSON_CONSTANT_KEY.IN),JSON_CONSTANT_VALUE.BODY)){
                map.put(param.getString(JSON_CONSTANT_KEY.NAME),defaultValue(param));
            }
        });
        return map;
    }

    /**
     * 获取formData参数
     *  只要不是path和header、body统一按from处理
     * @author mr.g
     * @param method 
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    public Map<String,String> getFormParam(String method) {
        Map<String,String> map = ChainMap.newMap();
        JSONArray json = methodsInfo.getJSONObject(method).getJSONArray(JSON_CONSTANT_KEY.PARAMETERS);
        json.forEach(str -> {
            JSONObject param = (JSONObject) str;
            String type = Fc.toStr(param.getString(JSON_CONSTANT_KEY.IN),JSON_CONSTANT_VALUE.QUERY);

            // 不是path和header、body参数并是必须得 或者 存在多个body得情况下，统一全部按from参数处理
            boolean isBody = isMultipleBody(json) && Fc.equals(type,JSON_CONSTANT_VALUE.BODY);
            if (
                    (param.getBoolean(JSON_CONSTANT_KEY.REQUIRED)
                        && !JSON_CONSTANT_VALUE.HEADER.concat("|").concat(JSON_CONSTANT_VALUE.PATH).concat("|".concat(JSON_CONSTANT_VALUE.BODY)).contains(type)
                    )
                    || isBody
               ){
                map.put(param.getString(JSON_CONSTANT_KEY.NAME),defaultValue(param));
            }
        });

        return map;
    }

    /**
     * 是否多个body
     * @author mr.g
     * @param params 参数列表
     * @return boolean
     */
    private boolean isMultipleBody(JSONArray params) {
        return params.stream().map(str -> (JSONObject) str).filter(param ->
                Fc.equals(param.getString(JSON_CONSTANT_KEY.IN), JSON_CONSTANT_VALUE.BODY)
//                        && param.containsKey(JSON_CONSTANT_KEY.SCHEMA) &&
//                    !param.getJSONObject(JSON_CONSTANT_KEY.SCHEMA).containsKey(JSON_CONSTANT_KEY.TYPE)
        ).count() > 1;
    }

    private String defaultValue(JSONObject param) {

        if (param.containsKey(JSON_CONSTANT_KEY.DEFAULT)){
            return param.getString(JSON_CONSTANT_KEY.DEFAULT);
        }


        if (param.containsKey(JSON_CONSTANT_KEY.SCHEMA) && StringUtil.equalsIgnoreCase(param.getString(JSON_CONSTANT_KEY.IN),JSON_CONSTANT_VALUE.BODY)){
            JSONObject schema = param.getJSONObject(JSON_CONSTANT_KEY.SCHEMA);
            return bodyValue(schema,0);
        }

        return formValue(param);
    }

    private String formValue(JSONObject param){
        String type = param.getString(JSON_CONSTANT_KEY.TYPE);

        switch (StringUtil.toLowerCase(type)) {
            case JSON_CONSTANT_VALUE.STRING :
                String format =  param.getString(JSON_CONSTANT_KEY.FORMAT);
                if (StringUtil.equalsIgnoreCase(format,JSON_CONSTANT_VALUE.DATE)){
                    return DateUtil.getDate(DateUtil.PATTERN_DATE);
                } else if (StringUtil.equalsIgnoreCase(format,JSON_CONSTANT_VALUE.DATE_TIME)){
                    return DateUtil.getDate(DateUtil.PATTERN_DATETIME);
                } else if (StringUtil.equalsIgnoreCase(format,JSON_CONSTANT_VALUE.TIME)){
                    return DateUtil.getDate(DateUtil.PATTERN_TIME);
                }
                return STRING_DEFAULT_VAL;
            case JSON_CONSTANT_VALUE.BOOLEAN :
                return StringPool.TRUE;
            case JSON_CONSTANT_VALUE.NUMBER :
            case JSON_CONSTANT_VALUE.INTEGER :
            default:
                return NUMBER_DEFAULT_VAL;

        }
    }

    /**
     * 获取body参数得值
     * @author mr.g
     * @param schema
     * @param level 循环层级，防止无限获取下去
     * @return java.lang.String
     */
    private String bodyValue(JSONObject schema, int level) {

        JSONObject json = new JSONObject();

        String originalRef = schema.getString(JSON_CONSTANT_KEY.ORIGINAL_REF);
        if (Fc.isNotBlank(originalRef)){
            definitions.getJSONObject(originalRef).getJSONObject(JSON_CONSTANT_KEY.PROPERTIES).forEach((k,v) -> {
                JSONObject attributes = (JSONObject) v;
                if (attributes.containsKey(JSON_CONSTANT_KEY.EXAMPLE)){
                    json.put(k,attributes.get(JSON_CONSTANT_KEY.EXAMPLE));
                }else if (attributes.containsKey(JSON_CONSTANT_KEY.$REF) && attributes.containsKey(JSON_CONSTANT_KEY.ORIGINAL_REF) && level <= 2){
                    json.put(k,bodyValue(attributes, level+1));
                }else if (JSON_CONSTANT_VALUE.NUMBER.concat("|").concat(JSON_CONSTANT_VALUE.INTEGER).contains(attributes.getString(JSON_CONSTANT_KEY.TYPE))){
                    json.put(k,0);
                }else if (JSON_CONSTANT_VALUE.BOOLEAN.contains(attributes.getString(JSON_CONSTANT_KEY.TYPE))){
                    json.put(k,StringPool.TRUE);
                }else {
                    json.put(k,StringPool.EMPTY);
                }
            });
        }else {
            return formValue(schema);
        }

        return JSON.toJSONString(json);
    }
}
