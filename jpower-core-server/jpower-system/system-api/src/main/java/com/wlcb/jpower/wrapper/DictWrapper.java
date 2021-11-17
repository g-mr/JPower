package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.dict.DictCache;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.GuavaCache;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.dictbind.handler.IDictBindHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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
     * 绑定字典
     * @author mr.g
     */
    @Override
    public void setMetaObject(Dict dict, Object fieldValue, MetaObject metaObject){
        if (Fc.isNotEmpty(fieldValue)){
            GuavaCache guavaCache = GuavaCache.getInstance(EXPIRE_TIME, TimeUnit.SECONDS);
            Object value = guavaCache.get(dict.name() + StringPool.COLON + fieldValue);
            if (Fc.isEmpty(value)){
                value = DictCache.getDictByTypeAndCode(dict.name(), Fc.toStr(fieldValue));
                if (Fc.isNotEmpty(value)){
                    guavaCache.put(dict.name() + StringPool.COLON + fieldValue,value);
                }
            }
            metaObject.setValue(dict.attributes(),value);
        }
    }
}
