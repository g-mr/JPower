package com.wlcb.jpower.module.support;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.utils.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DictSupport
 * @Description TODO 设置DICT值
 * @Author 郭丁志
 * @Date 2020/8/9 0009 23:03
 * @Version 1.0
 */
@Slf4j
public class DictSupport<T> {

    private static DictResult dictResult;

    static {
        dictResult = SpringUtil.contains("dictResult")?SpringUtil.getBean("dictResult"):null;
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
    public static List<Map<String,Object>> listDict(Collection<String> dictTypeNames){
//        List<Map<String,Object>> listDict = dictResult.queryDictList(new QueryWrapper<TbCoreDict>().lambda()
//                .select(TbCoreDict::getCode,TbCoreDict::getName,TbCoreDict::getDictTypeCode)
//                .in(TbCoreDict::getDictTypeCode,dictTypeNames));
        if (!Fc.isNull(dictResult)){
            List<Map<String,Object>> listDict = dictResult.queryDictList(new ArrayList<>(dictTypeNames));
            return listDict;
        }
        return null;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 把查询根据字典项分割
     * @Date 02:23 2020-07-18
     * @Param [dicts]
     * @return java.util.Map<java.lang.String,java.util.List<com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict>>
     **/
    public static Map<String,List<Map<String,Object>>> listDictMap(List<Map<String,Object>> dicts){
        Map<String,List<Map<String,Object>>> dictMap = new HashMap<>();
        if (dicts != null){
            for (Map<String,Object> tbCoreDict : dicts) {

                List<Map<String,Object>> dictList = dictMap.get(tbCoreDict.get("dict_type_code"));
                if (dictList == null){
                    dictList = new ArrayList<>();
                }
                dictList.add(tbCoreDict);
                dictMap.put(Fc.toStr(tbCoreDict.get("dict_type_code")),dictList);
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
    public static void setDict(Object t,List<Field> fields,Map<String,List<Map<String,Object>>> dictMap){
        try {
            for (Field field : fields) {
                Dict dict = field.getAnnotation(Dict.class);
                List<Map<String,Object>> dictList = dictMap.get(dict.name());

                if (dictList != null){
                    for (Map<String,Object> tbCoreDict : dictList) {

                        if (t instanceof Map){
                            Map<String, Object> map = (Map<String, Object>) t;
                            if (Fc.equals(tbCoreDict.get("code"),String.valueOf(map.get(StringUtil.humpToUnderline(field.getName()))))){
                                map.put(dict.attributes(),tbCoreDict.get("name"));
                            }
                        }else {
                            if (Fc.equalsValue(tbCoreDict.get("code"),ReflectUtil.getFieldValue(t,field.getName()))){
                                String attributesName = Fc.isBlank(dict.attributes())?field.getName():dict.attributes();
                                ReflectUtil.setFieldValue(t,attributesName,tbCoreDict.get("name"));
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
