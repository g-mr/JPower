package com.wlcb.jpower.cache.dict;

import cn.hutool.core.map.MapUtil;
import com.wlcb.jpower.feign.DictClient;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName DictConfig
 * @Description TODO 获取字典
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
public class DictCache {

    private static DictClient dictClient;

    static {
        dictClient = SpringUtil.getBean(DictClient.class);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 通过字典类型和编码直接返回值
     * @Date 17:28 2020-10-22
     * @Param [dictTypeCode, code]
     **/
    public static String getDictByTypeAndCode(String dictTypeCode, String code) {
        List<Map<String, Object>> list = getDictByType(dictTypeCode);
        list = Fc.isNull(list)?new ArrayList<>():list;
        return list.stream().map(t -> Fc.equals(t.get("code"),code)? MapUtil.getStr(t,"name") : StringPool.EMPTY).collect(Collectors.joining());
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 通过字典类型查询字典列表
     * @Date 17:29 2020-10-22
     * @Param [dictTypeCode]
     **/
    public static List<Map<String, Object>> getDictByType(String dictTypeCode) {
        return CacheUtil.get(CacheNames.DICT_REDIS_CACHE,CacheNames.DICT_TYPE_KEY,dictTypeCode,() -> {
            ResponseData<List<Map<String, Object>>> responseData = dictClient.queryDictByType(dictTypeCode);
            return responseData.getData();
        });
    }
}
