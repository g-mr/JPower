package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.gmac
 */
public class SqlInjectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(SqlInjectionUtil.class);

    private static final String[] SENSITIVE_CHAR = new String[]{"'","--","|","<",">"," or "," xor "," and ",
            "&lt;","&gt;","&#34","&#349","%27"};
    private static final String[] SQL_CHAR  = new String[]{"delete","drop","create","select","truncate","update","insert",
            "alter","declare","xp_cmdshell","exec","execute"};
    private static final String[] JS_CHAR  = new String[]{"<img","%3cimg","<script","%3cscript","alert","console","document.location","window.location","javascript"};

    /**
     * 过滤入口
     */
    public static String filter(String str) {
        String str0 = str;
        if (StringUtils.isBlank(str)) {
            return StringPool.EMPTY;
        }

        // 敏感字符
        for(String chars0: SENSITIVE_CHAR){
            if( str.toLowerCase().contains(chars0) ){
                str = str.replaceAll("(?i)"+chars0, StringPool.EMPTY);
            }
        }

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String keys0:SQL_CHAR){
            if( str.toLowerCase().contains(keys0+" ") ){
                str = str.replaceAll("(?i)"+keys0, StringPool.EMPTY);
            }
        }

        // 去掉含有歧义或者其他目的的关键词或语句
        for(String keys0:JS_CHAR){
            if( str.toLowerCase().replaceAll("\\s", StringPool.EMPTY).contains(keys0) ){
                str = str.replaceAll("(?i)"+keys0, StringPool.EMPTY);
            }
        }

        if( !str0.equals(str) ){
            logger.warn("检测到非法字符并已过滤。\n请求地址：{}\n原字符：{} \n新字符：{}",WebUtil.getRequest().getServletPath(),str0,str);
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
                if( null!= entry.getKey() && null!= entry.getValue() ){
                    mapNew.put(filter(entry.getKey()), filter(entry.getValue().toString()));
                }else{
                    if( null!= entry.getKey()){
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
        String valueNew;
        for (Map.Entry<String, String[]> entry : paras.entrySet()) {
            String[] values = entry.getValue();
            if (null == values) {
                value = "";
            } else if (values.length>1) {
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = values[0];
            }

            // 处理参数值：value
            valueNew = filter(value);
            url = url.replace(value, valueNew);
        }
        return url;
    }
}
