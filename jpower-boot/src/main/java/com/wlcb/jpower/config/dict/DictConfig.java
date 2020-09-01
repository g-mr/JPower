package com.wlcb.jpower.config.dict;

import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.support.DictResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ParamConfig
 * @Description TODO 获取配置文件参数
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
@Component("dictResult")
public class DictConfig implements DictResult {

    @Resource
    private CoreDictService dictService;

    @Override
    public List<Map<String, Object>> queryDictList(List<String> list) {

        List<Map<String, Object>> mapList = new ArrayList<>();
        List<String> listType = new ArrayList<>();
        
        list.forEach(type -> {

            List<Map<String, Object>> ls = CacheUtil.get(CacheNames.DICT_REDIS_CACHE,CacheNames.DICT_REDIS_TYPE_MAP_KEY,type);
            if (Fc.isNull(ls)){
                listType.add(type);
            }else {
                mapList.addAll(ls);
            }
            
        });

        if (listType.size() > 0){
            List<Map<String,Object>> data = dictService.dictListByTypes(listType);
            if (!Fc.isNull(data) && data.size() > 0){
                mapList.addAll(data);

                listType.forEach(lt -> CacheUtil.put(CacheNames.DICT_REDIS_CACHE,
                        CacheNames.DICT_REDIS_TYPE_MAP_KEY,
                        lt,
                        data.stream().filter(d -> Fc.equals(lt,(d.get("dict_type_code")))).collect(Collectors.toList()))
                );

            }
        }

        return mapList;
    }
}
