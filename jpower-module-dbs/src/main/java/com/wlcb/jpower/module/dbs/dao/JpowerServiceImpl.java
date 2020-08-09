package com.wlcb.jpower.module.dbs.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.support.DictSupport;
import com.wlcb.jpower.module.mp.support.Condition;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName JpowerServiceImpl
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 14:02
 * @Version 1.0
 */
public class JpowerServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * @author 郭丁志
     * @Description //TODO 为结果查询字典
     * @date 23:49 2020/8/9 0009
     * @param result 查询结果
     * @param queryWrapper 查询条件
     * @return void
     */
    private void queryDict(Object result, Wrapper<T> queryWrapper){
        if (Fc.isNull(result) || (result instanceof List && ((List) result).size() <= 0) ){
            return;
        }

        List<Field> fields = DictSupport.getDictFiled(queryWrapper.getSqlSelect(),this.getClass());
        if(fields.size() > 0){
            List<TbCoreDict> dicts = DictSupport.listDict(DictSupport.listDictName(fields));
            Map<String,List<TbCoreDict>> dictMap =  DictSupport.listDictMap(dicts);

            if (dictMap.size() > 0){
                if (result instanceof List){
                    ((List) result).forEach(i -> {
                        DictSupport.setDict(i,fields,dictMap);
                    });
                }else {
                    DictSupport.setDict(result,fields,dictMap);
                }
            }
        }

    }

    /**
     * 查询所有列表
     * todo 重写mybatis plus方法
     */
    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        List<Map<String, Object>> list = super.listMaps(queryWrapper);
        queryDict(list,queryWrapper);
        return list;
    }

    /**
     * 根据 Wrapper，查询一条记录
     * todo 重写mybatis plus 方法
     */
    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        Map<String, Object> map = super.getMap(queryWrapper);
        queryDict(map,queryWrapper);
        return map;
    }

    /**
     * 查询（根据ID）
     * todo 重写mybatisplus方法
     */
    @Override
    public T getById(Serializable id){
        T t = super.getById(id);
        queryDict(t,Wrappers.emptyWrapper());
        return t;
    }

    /**
     * 查询（根据ID 批量查询）
     * todo 重写mybatisplus方法
     */
    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        List<T> list = super.listByIds(idList);
        queryDict(list,Wrappers.emptyWrapper());
        return list;
    }

    @Override
    public List<T> listByMap(Map<String, Object> columnMap) {
        List<T> list = super.listByMap(columnMap);
        queryDict(list,Wrappers.emptyWrapper());
        return list;
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        T t = super.getOne(queryWrapper,throwEx);
        queryDict(t,queryWrapper);
        return t;
    }

    /**
     * 查询列表
     * todo 重写mybatisplus
     */
    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        List<T> list = super.list(queryWrapper);
        queryDict(list,queryWrapper);
        return list;
    }

    /**
     * 翻页查询
     *todo 重写mybatisplus
     */
    @Override
    public  <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        E e = super.page(page,queryWrapper);
        List<T> list = e.getRecords();
        queryDict(list,queryWrapper);
        e.setRecords(list);
        return e;
    }

    /**
     * 翻页查询
     * todo 重写mybatisplus
     */
    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper) {
        E e =  super.pageMaps(page,queryWrapper);
        List<Map<String, Object>> list = e.getRecords();
        queryDict(list,queryWrapper);
        e.setRecords(list);
        return e;
    }

    /**
     * 加载树形节点
     *
     * @param treeWrapper
     * @return
     */
    public List<Node> tree(Wrapper<T> treeWrapper) {
        List<Map<String,Object>> list = listMaps(treeWrapper);
        return ForestNodeMerger.merge(list.stream().filter(Objects::nonNull).map(Condition.TreeWrapper::createNode).collect(Collectors.toList()));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 把查询结果转换成任何类型
     * @Date 21:22 2020-07-30
     * @Param [queryWrapper, mapper]
     * @return java.util.List<V>
     **/
    public  <V> List<V> listToObjs(Wrapper<T> queryWrapper, Function<T, V> mapper) {
        return list(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询树形结构list
     *                  查询字段中必须包含code和parentCode字段，否则无法形成tree列表
     * @Date 21:51 2020-07-30
     * @Param [queryWrapper, clz]
     * @return java.util.List<V>
     **/
    public <V extends Node> List<V> listTree(Wrapper<T> queryWrapper,Class<V> clz) {
        Function<T, V> mapper = new Function<T, V>() {
            @Override
            public V apply(T t) {
                return (V) BeanUtil.copy(t, clz);
            }
        };
        return ForestNodeMerger.merge(listToObjs(queryWrapper,mapper));
    }

    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     *
     * 链式查询暂未支持查询字典
     */
    @Override
    public QueryChainWrapper<T> query() {
        return ChainWrappers.queryChain(getBaseMapper());
    }

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     *
     * 链式查询暂未支持查询字典
     */
    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        return ChainWrappers.lambdaQueryChain(getBaseMapper());
    }

}
