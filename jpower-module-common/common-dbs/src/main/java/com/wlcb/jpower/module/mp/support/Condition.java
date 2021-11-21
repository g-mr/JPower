package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.node.TreeNode;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;

/**
 * @ClassName Condition
 * @Description TODO 扩展QueryWrapper
 * @Author 郭丁志
 * @Date 2020-07-23 15:01
 * @Version 1.0
 */
public class Condition<T> {

    public Condition() {
    }

    public static <T> QueryWrapper<T> getQueryWrapper() {
        return new QueryWrapper<T>();
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> clz) {
        QueryWrapper<T> qw = new QueryWrapper();
        qw.setEntity(BeanUtil.newInstance(clz));
        return qw;
    }

    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<T>(entity);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        ChainMap exclude = ChainMap.init().set("pageNum", "pageNum").set("pageSize", "pageSize").set("asc", "asc").set("desc", "desc");
        return getQueryWrapper(query, exclude, clazz);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
        exclude.forEach((k, v) -> {
            query.remove(k);
        });
        QueryWrapper<T> qw = new QueryWrapper();
        qw.setEntity(BeanUtil.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

    public static <T> TreeWrapper<T> getTreeWrapper(SFunction<T, ?> code, SFunction<T, ?> parentCode, SFunction<T, ?> title)  {
        return new TreeWrapper<T>(code,parentCode,title);
    }

    public static <T> TreeWrapper<T> getTreeWrapper(SFunction<T, ?> code, SFunction<T, ?> parentCode, SFunction<T, ?> title, SFunction<T, ?> value)  {
        return new TreeWrapper<T>(code,parentCode,title,value);
    }

    public static <T> TreeWrapper<T> getTreeWrapper(SFunction<T, ?> code, SFunction<T, ?> parentCode, SFunction<T, ?> title, SFunction<T, ?> key , SFunction<T, ?> value)  {
        return new TreeWrapper<T>(code,parentCode,title,key,value);
    }

    public static class TreeWrapper<T> extends QueryWrapper<T> {

        private String code;
        private String parentCode;
        private String title;
        private String value;
        private String id;
        private String tableName;

        /**
         * @author 郭丁志
         * @Description //TODO 查询树形菜单组装
         * @date 15:59 2020/7/26 0026
         * @param code 节点编号
         * @param parentCode 上级节点编号
         * @param title 节点名称
         * @return
         */
        public TreeWrapper(SFunction<T, ?> code,SFunction<T, ?> parentCode,SFunction<T, ?> title){
            this(code, parentCode, title, null);
        }

        public TreeWrapper(SFunction<T, ?> code,SFunction<T, ?> parentCode,SFunction<T, ?> title, SFunction<T, ?> value){
            this(code, parentCode, title, null, value);
        }

        public TreeWrapper(SFunction<T, ?> code,SFunction<T, ?> parentCode,SFunction<T, ?> title,SFunction<T, ?> key,SFunction<T, ?> value){
            TableInfo tableInfo = TableInfoHelper.getTableInfo(LambdaUtils.extract(title).getInstantiatedClass());
            this.tableName = tableInfo.getTableName();

            this.id = tableInfo.getKeyColumn() + " AS id";
            this.value = tableInfo.getKeyColumn() + " AS value";
            this.code = tableInfo.getKeyColumn() + " AS code";
            tableInfo.getFieldList().forEach(field -> {
                if (StringUtil.equals(field.getProperty(),columnsToString(code))){
                    this.code = field.getColumn() + " AS code";
                }
                if (StringUtil.equals(field.getProperty(),columnsToString(parentCode)) ){
                    this.parentCode = field.getColumn() + " AS pcode";
                }
                if (StringUtil.equals(field.getProperty(),columnsToString(title))){
                    this.title = field.getColumn() + " AS title";
                }
                if (!Fc.isNull(value) && StringUtil.equals(field.getProperty(),columnsToString(value))){
                    this.value = field.getColumn() + " AS value";
                }
                if (!Fc.isNull(key) && StringUtil.equals(field.getProperty(),columnsToString(key))){
                    this.id = field.getColumn() + " AS id";
                }
            });

            this.tree();
        }

        /**
         * @author 郭丁志
         * @Description //TODO 加载树形结构
         * @date 15:51 2020/7/26 0026
         * @param
         * @return com.wlcb.jpower.module.mp.support.Condition.TreeWrapper<T>
         */
        private void tree() {
            select(this.code,this.parentCode,this.title,this.value,this.id);
        }

        /**
         * @author 郭丁志
         * @Description //TODO 懒加载树形结构，给定pcode值
         * @date 15:53 2020/7/26 0026
         * @param pcodeVal
         * @return com.wlcb.jpower.module.mp.support.Condition.TreeWrapper<T>
         */
        public TreeWrapper<T> lazy(String pcodeVal){
            select("( SELECT CASE WHEN count( 1 ) > 0 THEN 1 ELSE 0 END FROM "+tableName+" as c WHERE "+StringUtil.splitTrim(this.parentCode,"AS").get(0)+" = "+tableName+"."+StringUtil.splitTrim(this.code,"AS").get(0)+" ) AS hasChildren",
                    this.code,
                    this.parentCode,
                    this.title,
                    this.value,
                    this.id);
            eq(StringUtil.splitTrim(this.parentCode,"AS").get(0),StringUtil.isBlank(pcodeVal)?JpowerConstants.TOP_CODE:pcodeVal);
            return this;
        }

        /**
         * @author 郭丁志
         * @Description //TODO MAP过滤条件，也可支持原始的warpper查询
         * @date 15:54 2020/7/26 0026
         * @param query
         * @return com.wlcb.jpower.module.mp.support.Condition.TreeWrapper<T>
         */
        public TreeWrapper<T> map(Map<String,Object> query){
            SqlKeyword.buildCondition(query, this);
            return this;
        }

        private String columnsToString(SFunction<T, ?> column) {
            return PropertyNamer.methodToProperty(LambdaUtils.extract(column).getImplMethodName());
        }

        /**
         * @author 郭丁志
         * @Description //TODO 构造树形节点
         * @date 16:10 2020/7/26 0026
         * @param map
         * @return com.wlcb.jpower.module.common.node.Node
         */
        public static Node createNode(Map<String, Object> map) {
            TreeNode node = new TreeNode();
            node.setId(Fc.toStr(map.get("code")));
            node.setKey(Fc.toStr(map.get("id")));
            node.setValue(Fc.toStr(map.get("value")));
            node.setParentId(Fc.toStr(map.get("pcode")));
            node.setTitle(Fc.toStr(map.get("title")));
            node.setHasChildren(Fc.toBool(map.get("hasChildren")));
            return node;
        }

    }
}
