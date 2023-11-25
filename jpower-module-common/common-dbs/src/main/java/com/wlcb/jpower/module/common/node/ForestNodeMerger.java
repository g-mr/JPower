package com.wlcb.jpower.module.common.node;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.TypeUtil;
import com.wlcb.jpower.module.common.utils.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORT;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORTNUM;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORT_NUM;

/**
 * @author mr.g
 * @Desc TODO 森林节点归并类
 * @Date 2020-07-25 22:45
 */
public class ForestNodeMerger {

    public static final TreeNodeConfig CONFIG = new TreeNodeConfig();

    public static final String HAS_CHILDREN = "hasChildren";


    static {
        CONFIG.setWeightKey(SORT);
    }

    @SuppressWarnings("unchecked")
    public static <T,E> List<Tree<E>> mergeTree(List<T> list) {
        if (Fc.isEmpty(list)){
            return new ArrayList<>();
        }

        List<Tree<E>> listTree = TreeUtil.build( list, getRootId(list), CONFIG,  (bean, tree) -> {
            Map<String, Object> extra;
            if (bean instanceof Map){
                extra = (Map<String, Object>) bean;
            } else {
                extra = BeanUtil.beanToMap(bean);
            }

            if (!extra.containsKey(SORT) && MapUtil.containsAnyKey(extra, SORTNUM, SORT_NUM)){
                extra.put(SORT, MapUtil.getAny(extra, SORTNUM, SORT_NUM).get(SORTNUM));
                MapUtil.removeAny(extra, SORTNUM, SORT_NUM);
            }

            if(MapUtil.isNotEmpty(extra)){
                extra.forEach((k,v) -> {
                    String key = StringUtil.underlineToHump(k);
                    if (Fc.equals(key,HAS_CHILDREN)){
                        v = Fc.toBool(v,false);
                    }
                    tree.putExtra(key,v);
                });
            }
        });

        listTree = completionHasChildren(listTree);
        return listTree;
    }

    private static <E> List<Tree<E>> completionHasChildren(List<Tree<E>> listTree) {

        if (Fc.isEmpty(listTree)){
            return new ArrayList<>();
        }

        return listTree.stream().peek(map->{
            if (Fc.isNotEmpty(map.getChildren())){
                completionHasChildren(map.getChildren());
                map.put(HAS_CHILDREN,true);
            }else {
                map.put(HAS_CHILDREN,Fc.toBool(map.get(HAS_CHILDREN),false));
            }
        }).collect(Collectors.toList());
    }

    /**
     * 获取顶级ID
     * @Author mr.g
     * @param list
     * @return java.lang.String
     **/
    private static <T,E extends Serializable> E getRootId(List<T> list) {
        List<E> rootIds =  list.parallelStream().map(ForestNodeMerger::beanToMap).filter(map -> list.stream().noneMatch(i->{
                    Map<String, Object> map1 = beanToMap(i);
                    return Fc.equalsValue(map1.get(CONFIG.getIdKey()),map.get(CONFIG.getParentIdKey()));
                }))
                .map(map->{
                    return MapUtil.get(map, CONFIG.getParentIdKey(), new TypeReference<E>() {
                        public Type getType() {
                            return TypeUtil.getReturnType(ReflectUtil.getMethodByName(ForestNodeMerger.class, "getRootId"));
                        }
                    });
                }).distinct().collect(Collectors.toList());

        if (Fc.isEmpty(rootIds)){
            throw new ConvertException("list to tree =>> rootId not found, suspect circular dependency");
        }
        if (rootIds.size() > 1){
            throw new ConvertException("list to tree =>> Suspect the existence of multiple rootId");
        }

        return rootIds.get(0);

    }

    /**
     * bean转MAP
     * @Author mr.g
     * @param t
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private static <T> Map<String, Object> beanToMap(T t) {
        Map<String, Object> map = BeanUtil.beanToMap(t);
        if (!map.containsKey(CONFIG.getIdKey())){
            MapUtil.renameKey(map,StringUtil.humpToUnderline(CONFIG.getIdKey()),CONFIG.getIdKey());
        }
        if (!map.containsKey(CONFIG.getParentIdKey())){
            MapUtil.renameKey(map,StringUtil.humpToUnderline(CONFIG.getParentIdKey()),CONFIG.getParentIdKey());
        }
        return map;
    }
}
