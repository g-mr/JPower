package top.jpower.jpower.cache.dict;

import top.jpower.common.enums.YYZLEnum;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.jpower.feign.DictClient;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;

/**
 * 字典缓存
 *
 * @author mr.g
 **/
public class DictCache {

    private static DictClient dictClient;

    static {
        dictClient = SpringUtil.getBean(DictClient.class);
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
                .filter(map -> {

                    String requestLocale = YYZLEnum.CHINA.getValue();
                    if (Fc.notNull(WebUtil.getRequest())){
                        requestLocale = Fc.toStr(Objects.requireNonNull(WebUtil.getRequest()).getHeader(I18N_KEY), YYZLEnum.CHINA.getValue());
                    }

                    return Fc.equalsValue(MapUtil.getStr(map,"code"),code) && Fc.equalsValue(MapUtil.getStr(map,"locale"), requestLocale);
                })
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
            ResponseData<List<Map<String, Object>>> responseData = dictClient.queryDictByType(dictTypeCode);
            return responseData.getData();
        });
    }
}
