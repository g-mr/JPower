package com.wlcb.jpower.test;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.wlcb.jpower.module.common.utils.JsonUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/11/23 0023 23:41
 */
public class TreeTest {

    public static void main(String[] args) {
//        TbCoreCity coreCity = new TbCoreCity();
//        coreCity.setId("1");
//        coreCity.setPcode("-1");

        Map<String,Object> map = new HashMap<>();
        map.put("id","123");
        map.put("parentId","-1");
        map.put("title","测试");
        map.put("value",false);
        map.put("key","DKFS");

        Map<String,Object> map1 = new HashMap<>();
        map1.put("id","133");
        map1.put("parentId","-1");
        map1.put("title","备注");
        map1.put("value",true);
        map1.put("key","SDDF");

        Map<String,Object> map2 = new HashMap<>();
        map2.put("id","1335");
        map2.put("parentId","133");
        map2.put("title","不知带哦");
        map2.put("value",true);
        map2.put("key","DSD");

        Map<String,Object> map3 = new HashMap<>();
        map3.put("id","13325");
        map3.put("parentId","1335");
        map3.put("title","不带哦");
        map3.put("value",true);
        map3.put("key","DSDDF");

        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        list.add(map1);
        list.add(map2);
        list.add(map3);



//        TreeNodeConfig DEFAULT_CONFIG = new TreeNodeConfig();
//        DEFAULT_CONFIG.setIdKey("code");
//        DEFAULT_CONFIG.setParentIdKey("pcode");
//        DEFAULT_CONFIG.setNameKey("title");


        List<Tree<String>> treeNodes = TreeUtil.build( list, JpowerConstants.TOP_CODE, TreeNodeConfig.DEFAULT_CONFIG, (treeNode, tree) -> {
//            tree.setId(Fc.toStr(treeNode.get("code")));
//            tree.setParentId(Fc.toStr(treeNode.get("pcode")));
////            tree.setWeight(treeNode.getWeight());
//            tree.setName(Fc.toStr(treeNode.get("title")));
//
//            treeNode.remove("code");
//            treeNode.remove("pcode");
//            treeNode.remove("title");

            //扩展字段
            final Map<String, Object> extra = treeNode;
            if(MapUtil.isNotEmpty(extra)){
                extra.forEach(tree::putExtra);
            }
        });

        System.out.println(JsonUtil.toJson(treeNodes));
    }

}
