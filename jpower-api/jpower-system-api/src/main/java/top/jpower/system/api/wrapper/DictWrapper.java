package top.jpower.system.api.wrapper;

import top.jpower.core.dbs.dictbind.handler.IDictBindHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.system.api.cache.dict.DictCache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 字典查询
 *
 * @author mr.g
 */
@Component
public class DictWrapper implements IDictBindHandler {

    /**
     * 本地缓存过期时间为5秒
     */
    private final static Long EXPIRE_TIME = 5L;
    /**
     * 字典默认存放字段名称
     */
    private final static String DICT_PARAMS = "params";

    /**
     * 绑定字典
     * @author mr.g
     */
    @Override
    public void setMetaObject(Dict dict, String fieldName, Object fieldValue, MetaObject metaObject){
        if (Fc.isNotEmpty(fieldValue)){
            if (Fc.isNotBlank(dict.name())){
                GuavaCache<List<Map<String, Object>>> guavaCache = GuavaCache.getInstance(EXPIRE_TIME, TimeUnit.SECONDS);
                List<Map<String, Object>> list;
                if (guavaCache.isExist(dict.name())){
                    list = guavaCache.get(dict.name());
                }else {
                    list = DictCache.getDictByType(dict.name());
                    guavaCache.put(dict.name(),list);
                }

                String value = null;
                if (Fc.isNotEmpty(list)){
                    value = list.stream()
                            .filter(map -> Fc.equalsValue(MapUtil.getStr(map,"code"),fieldValue))
                            .map(map-> MapUtil.getStr(map,"name"))
                            .collect(Collectors.joining(StringPool.SPILT));
                }

                if (Fc.isNotBlank(dict.attributes())){
                    metaObject.setValue(dict.attributes(),value);
                } else {
                    if (metaObject.hasGetter(DICT_PARAMS)){
                        ((Map)metaObject.getValue(DICT_PARAMS)).put(fieldName,value);
                    }
                }

            }
        }
    }
}
