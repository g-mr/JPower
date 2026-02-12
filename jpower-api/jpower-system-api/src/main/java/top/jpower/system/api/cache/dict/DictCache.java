package top.jpower.system.api.cache.dict;

import top.jpower.system.api.feign.DictClient;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典缓存
 *
 * @author mr.g
 **/
public class DictCache {

    private static final DictClient DICT_CLIENT;

    static {
        DICT_CLIENT = SpringUtil.getBean(DictClient.class);
    }

    /**
     * 通过字典类型和编码直接返回值
     *
     * @author mr.g
     * @param dictTypeCode 字典类型CODE
     * @param code CODE
     * @return 字典值
     **/
    public static String getDictByTypeAndCode(String dictTypeCode, String code) {
        List<Map<String, Object>> list = getDictByType(dictTypeCode);
        list = Fc.isNull(list)?new ArrayList<>():list;
        return list.stream()
                .filter(map -> Fc.equalsValue(MapUtil.getStr(map,"code"),code))
                .map(map-> MapUtil.getStr(map,"name"))
                .collect(Collectors.joining(StringPool.SPILT));
    }

    /**
     * 通过字典类型查询字典列表
     *
     * @author mr.g
     * @param dictTypeCode 字典类型CODE
     * @return 字典列表
     **/
    public static List<Map<String, Object>> getDictByType(String dictTypeCode) {
        return CacheUtil.get(CacheNames.DICT_KEY,CacheNames.DICT_TYPE_KEY,dictTypeCode,() -> {
            R<List<Map<String, Object>>> r = DICT_CLIENT.queryDictByType(dictTypeCode);
            return r.getData();
        });
    }
}
