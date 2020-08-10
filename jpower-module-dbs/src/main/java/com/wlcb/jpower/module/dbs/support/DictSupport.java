package com.wlcb.jpower.module.dbs.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.dbs.dao.core.dict.mapper.TbCoreDictMapper;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DictSupport
 * @Description TODO 查询DICT支撑类
 * @Author 郭丁志
 * @Date 2020/8/9 0009 23:03
 * @Version 1.0
 */
@Slf4j
public class DictSupport<T> {

    private static TbCoreDictMapper coreDictMapper;

    static {
        coreDictMapper = SpringUtil.getBean(TbCoreDictMapper.class);
    }

    public static List<Field> getDictFiled(String selectSql,Class cls){
        List<Field> fields = BeanUtil.getFiledHaveAnnotation(ReflectUtil.getClassGenricType(cls,1), Dict.class);
        //selectSql为空代表查询所有的字段，所以为空的时候也算
        return fields.stream().filter(field->
                Fc.isBlank(selectSql)||selectSql.contains(StringUtil.humpToUnderline(field.getName()))
        ).collect(Collectors.toList());
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 返回所有的字典类型名称 并去重
     * @Date 02:21 2020-07-18
     * @Param [mapFieldDicts]
     * @return java.util.Set<java.lang.String>
     **/
    public static Set<String> listDictName(List<Field> fields){
        Set<String> typeNames = new HashSet<>();
        fields.forEach(field -> typeNames.add(field.getAnnotation(Dict.class).name()));
        return typeNames;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询所有字典
     * @Date 02:21 2020-07-18
     * @Param [dictTypeNames]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict>
     **/
    public static List<TbCoreDict> listDict(Collection<String> dictTypeNames){
        List<TbCoreDict> listDict = coreDictMapper.selectList(new QueryWrapper<TbCoreDict>().lambda()
                .select(TbCoreDict::getCode,TbCoreDict::getName,TbCoreDict::getDictTypeCode)
                .in(TbCoreDict::getDictTypeCode,dictTypeNames));
        return listDict;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 把查询根据字典项分割
     * @Date 02:23 2020-07-18
     * @Param [dicts]
     * @return java.util.Map<java.lang.String,java.util.List<com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict>>
     **/
    public static Map<String,List<TbCoreDict>> listDictMap(List<TbCoreDict> dicts){
        Map<String,List<TbCoreDict>> dictMap = new HashMap<>();
        if (dicts != null){
            for (TbCoreDict tbCoreDict : dicts) {

                List<TbCoreDict> dictList = dictMap.get(tbCoreDict.getDictTypeCode());
                if (dictList == null){
                    dictList = new ArrayList<>();
                }
                dictList.add(tbCoreDict);
                dictMap.put(tbCoreDict.getDictTypeCode(),dictList);
            }
        }
        return dictMap;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 给结果赋值
     * @Date 03:23 2020-07-19
     * @Param [mapFieldDicts, dictMap, t]
     **/
    public static void setDict(Object t,List<Field> fields,Map<String,List<TbCoreDict>> dictMap){
        try {
            for (Field field : fields) {
                Dict dict = field.getAnnotation(Dict.class);
                List<TbCoreDict> dictList = dictMap.get(dict.name());

                if (dictList != null){
                    for (TbCoreDict tbCoreDict : dictList) {

                        if (t instanceof Map){
                            Map<String, Object> map = (Map<String, Object>) t;
                            if (StringUtils.equals(tbCoreDict.getCode(),String.valueOf(map.get(StringUtil.humpToUnderline(field.getName()))))){
                                map.put(dict.attributes(),tbCoreDict.getName());
                            }
                        }else {
                            if (Fc.equalsValue(tbCoreDict.getCode(),ReflectUtil.getFieldValue(t,field.getName()))){
                                String attributesName = StringUtils.isBlank(dict.attributes())?field.getName():dict.attributes();
                                ReflectUtil.setFieldValue(t,attributesName,tbCoreDict.getName());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("设置DICT失败，error={}",ExceptionsUtil.getStackTraceAsString(e));
        }
    }

}
