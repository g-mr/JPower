package top.jpower.jpower.module.common.utils;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.jpower.module.common.constants.ClientNameConstant;

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
            JpowerProperties.SERVER server = SpringUtil.getBean(JpowerProperties.class).getServer();
            if (server == JpowerProperties.SERVER.BOOT){
                try {
                    map = SpringUtil.getBean(JdbcTemplate.class).queryForMap("select name,client_code as clientCode,client_secret as clientSecret,access_token_validity as accessTokenValidity,refresh_token_validity as refreshTokenValidity from tb_core_client where client_code = ?",code);
                } catch (EmptyResultDataAccessException e){
                    map = null;
                }
            }else {
                ResponseData<Map<String,Object>> responseData = SpringUtil.getBean(RestTemplate.class).getForObject("http://"+ ClientNameConstant.getInstance().getJpowerSystem()+"/core/client/getClientByClientCode?clientCode="+code, ResponseData.class);
                map = responseData.getData();
            }
            CLIENT_CACHE.put(code,map);
        }
        if (Fc.isEmpty(map)){
            return null;
        }
        return MapUtil.getStr(map,"clientSecret");
    }

}
