package com.wlcb.jpower.module.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlInjectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(SqlInjectionUtil.class);

    static final String[] char0  = new String[]{"'","/*","*/","--","|","<",">"," or "," xor "," and ",
            "&lt;","&gt;","&#34","&#349","%27"};
    static final String[] keys1  = new String[]{"delete","drop","create","select","truncate","update","insert",
            "alter","declare","xp_cmdshell","exec","execute"};
    static final String[] keys2  = new String[]{"<img","%3cimg","<script","%3cscript","alert","console","document.location","window.location","javascript"};

    /**
     * 过滤入口
     */
    public static String filter(String str) {
        return filter2(str);
    }


    public static String filter2(String str) {
        String str0 = str;
        if (StringUtils.isBlank(str)) {
            return "";
        }

        // 敏感字符
        for(String chars0:char0){
            if( str.toLowerCase().contains(chars0) ){
                str = str.replaceAll("(?i)"+chars0, "");
            }
        }

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String keys0:keys1){
            if( str.toLowerCase().contains(keys0+" ") ){
                str = str.replaceAll("(?i)"+keys0, "");
            }
        }

        // 去掉含有歧义或者其他目的的关键词或语句
        for(String keys0:keys2){
            if( str.toLowerCase().replaceAll("\\s", "").contains(keys0) ){
                str = str.replaceAll("(?i)"+keys0, "");
            }
        }

        if( !str0.equals(str) ){
            logger.warn("检测到非法字符并已过滤。\n原字符：{} \n新字符：{}",str0,str);
        }
        return str;
    }

    /**
     * 过滤 Map 中的所有key和values
     * @param map
     * @param filterKey 是否过滤key
     * @return
     */
    public static Map<String, Object>  filterMap(Map<String, Object> map,boolean filterKey) {
        Map<String, Object> mapNew = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(filterKey){
                if( null!= entry.getKey() && null!= entry.getValue() ){ // 键值均不为空
                    mapNew.put(filter(entry.getKey()), filter(entry.getValue().toString()));
                }else{
                    if( null!= entry.getKey()){ // 键 不为空 ，值为空
                        mapNew.put(filter(entry.getKey()), null);
                    }
                }
            }else{
                if( null!= entry.getValue()){
                    mapNew.put( entry.getKey() , filter(entry.getValue().toString()));
                }else{
                    mapNew.put( entry.getKey() , null);
                }
            }

        }

        return mapNew;
    }


    /**
     * 过滤URL参数值
     * @param url 整个URL
     * @param paras 使用“httpServletRequest.getParameterMap()”获取的URL的参数对
     * @return
     */
    public static String filterParameters(String url,Map<String, String[]> paras) {
        String value = "";
        String valueNew = "";
        for (Map.Entry<String, String[]> entry : paras.entrySet()) {
            String[] values = entry.getValue();
            if (null == values) {
                value = "";
            } else if (values.length>1) {
                for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = values[0];//用于请求参数中请求参数名唯一
            }

            // 处理参数值：value
            valueNew = filter(value);
            url = url.replace(value, valueNew);
        }
        return url;
    }

    /**
     * java实现不区分大小写替换
     * @param source
     * @param oldString
     * @param newString
     * @return
     */
    public static String IgnoreCaseReplace(String source, String oldString,String newString){
        Pattern p = Pattern.compile(oldString, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        String ret=m.replaceAll(newString);
        return ret;
    }
}
