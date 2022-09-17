package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2022-09-07 19:45
 */
public class SystemClient {

    private static final GuavaCache<Map<String,Object>> CLIENT_CACHE = GuavaCache.getInstance(1L, TimeUnit.MINUTES);

    public static String client(String code){

        Map<String,Object> map = CLIENT_CACHE.get(code);
        if (Fc.isNull(map)){
            ResponseData<Map<String,Object>> responseData = SpringUtil.getBean(RestTemplate.class).getForObject("http://"+ AppConstant.JPOWER_SYSTEM+"/core/client/getClientByClientCode?clientCode="+code, ResponseData.class);
            map = responseData.getData();
            CLIENT_CACHE.put(code,map);
        }
        if (Fc.isEmpty(map)){
            return null;
        }
        return MapUtil.getStr(map,"clientSecret");
    }

}
