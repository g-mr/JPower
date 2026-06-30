package com.qidiangk.smart.system.api.cache.dict;

import cn.hutool.core.text.StrPool;
import com.qidiangk.smart.common.constants.CacheNames;
import com.qidiangk.smart.common.enums.YYZLEnum;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.WebUtil;
import com.qidiangk.smart.system.api.dto.SelectDTO;
import com.qidiangk.smart.system.api.feign.DictClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;

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
        List<SelectDTO> list = getDictByType(dictTypeCode);
        list = Fc.isNull(list)?new ArrayList<>():list;
        return list.stream()
                .filter(map -> Fc.equalsValue(map.getCode(),code))
                .map(SelectDTO::getName)
                .collect(Collectors.joining(StringPool.SPILT));
    }

    /**
     * 通过字典类型查询字典列表
     *
     * @author mr.g
     * @param dictTypeCode 字典类型CODE
     * @return 字典列表
     **/
    public static List<SelectDTO> getDictByType(String dictTypeCode) {
		String requestLocale = Fc.toStr(WebUtil.getHeader(I18N_KEY), YYZLEnum.CHINA.getValue());
        return CacheUtil.get(CacheNames.DICT_KEY, CacheNames.DICT_TYPE_KEY, requestLocale + StrPool.COLON + dictTypeCode,() -> {
            R<List<SelectDTO>> r = DICT_CLIENT.queryDictByType(dictTypeCode);
            return r.getData();
        });
    }
}
