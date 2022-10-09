package com.wlcb.jpower.cache.dict;

import com.wlcb.jpower.feign.DictClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.common.utils.constants.ConstantsUtils.I18N_KEY;

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

                    String requestLocale = ConstantsEnum.YYZL.CHINA.getValue();
                    if (Fc.notNull(WebUtil.getRequest())){
                        requestLocale = Fc.toStr(Objects.requireNonNull(WebUtil.getRequest()).getHeader(I18N_KEY), ConstantsEnum.YYZL.CHINA.getValue());
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
