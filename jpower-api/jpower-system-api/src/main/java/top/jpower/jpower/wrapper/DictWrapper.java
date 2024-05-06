package top.jpower.jpower.wrapper;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.jpower.common.enums.YYZLEnum;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.jpower.cache.dict.DictCache;
import top.jpower.jpower.module.annotation.Dict;
import top.jpower.jpower.module.dictbind.handler.IDictBindHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;

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
                            .filter(map -> {
                                String requestLocale = YYZLEnum.CHINA.getValue();
                                if (Fc.notNull(WebUtil.getRequest())){
                                    requestLocale = Fc.toStr(Objects.requireNonNull(WebUtil.getRequest()).getHeader(I18N_KEY), YYZLEnum.CHINA.getValue());
                                }
                                return Fc.equalsValue(MapUtil.getStr(map,"code"),fieldValue) && Fc.equalsValue(MapUtil.getStr(map,"locale"), requestLocale);
                            })
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
