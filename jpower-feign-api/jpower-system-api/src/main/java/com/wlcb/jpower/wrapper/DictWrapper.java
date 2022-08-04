package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.dict.DictCache;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.GuavaCache;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.dictbind.handler.IDictBindHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author mr.g
 * @Date 2021/11/16 0016 0:56
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
                GuavaCache<String> guavaCache = GuavaCache.getInstance(EXPIRE_TIME, TimeUnit.SECONDS);
                String value = guavaCache.get(dict.name() + StringPool.COLON + fieldValue);
                if (Fc.isBlank(value)){
                    value = DictCache.getDictByTypeAndCode(dict.name(), Fc.toStr(fieldValue));
                    if (Fc.isNotBlank(value)){
                        guavaCache.put(dict.name() + StringPool.COLON + fieldValue,value);
                    }
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
