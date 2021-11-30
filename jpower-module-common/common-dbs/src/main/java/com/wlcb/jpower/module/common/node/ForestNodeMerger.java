package com.wlcb.jpower.module.common.node;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORT;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORTNUM;

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

    public static <T> List<Tree<String>> mergeTree(List<T> list) {
        List<Tree<String>> listTree = TreeUtil.build( list, getRootId(list), CONFIG,  (bean, tree) -> {
            Map<String, Object> extra;
            if (bean instanceof Map){
                extra = (Map<String, Object>) bean;
            }else {
                extra = BeanUtil.beanToMap(bean);
            }

            if (extra.containsKey(SORTNUM)){
                extra.put(SORT,extra.get(SORTNUM));
                extra.remove(SORTNUM);
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

    private static List<Tree<String>> completionHasChildren(List<Tree<String>> listTree) {

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
    private static <T> String getRootId(List<T> list) {
        AtomicReference<String> rootId = new AtomicReference<>(JpowerConstants.TOP_CODE);
        if (Fc.isNotEmpty(list)){
            list.stream().forEach(t -> {
                Map<String, Object> map = beanToMap(t);
                long count = list.stream().filter(i->{
                    Map<String, Object> map1 = beanToMap(i);
                    return Fc.equals(map1.get(CONFIG.getIdKey()),map.get(CONFIG.getParentIdKey()));
                }).count();
                if (count <= 0){
                    rootId.set(Fc.toStr(map.get(CONFIG.getParentIdKey()),JpowerConstants.TOP_CODE));
                    return;
                }
            });
        }
        return rootId.get();
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
