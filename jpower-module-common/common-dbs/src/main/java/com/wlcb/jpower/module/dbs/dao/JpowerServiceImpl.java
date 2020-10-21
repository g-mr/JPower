package com.wlcb.jpower.module.dbs.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.support.DictSupport;
import com.wlcb.jpower.module.tenant.TenantConstant;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName JpowerServiceImpl
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 14:02
 * @Version 1.0
 */
public class JpowerServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    /**
     * @author 郭丁志
     * @Description //TODO 设置实体值
     * @date 2:47 2020/10/18 0018
     */
    private void resolveEntity(T entity){
        // todo start 这里获取实际登陆人，如果是匿名用户或者其他登陆端，交给mp来赋值（可应用于未登录状态，根据具体业务前端可控制这些字段来保存更加符合的值）
        String userId = SecureUtil.getUserId();
        userId = Fc.isBlank(userId)?null:userId;
        Date now = new Date();
        if (Fc.isBlank(entity.getId())){
            entity.setCreateTime(now);
            entity.setCreateUser(userId);
            entity.setIsDeleted(JpowerConstants.DB_NOT_DELETE);
        }

        entity.setUpdateUser(userId);
        entity.setUpdateTime(now);
        // todo end

        if (SecureUtil.isRoot()){
            Field field = ReflectUtil.getField(entity.getClass(), TenantConstant.TENANT_CODE);
            if (ObjectUtil.isNotEmpty(field)){
                String tenantCode = ReflectUtil.invokeGetter(entity,TenantConstant.TENANT_CODE);
                if (Fc.isBlank(tenantCode)){
                    ReflectUtil.invokeSetter(entity,TenantConstant.TENANT_CODE,TenantConstant.DEFAULT_TENANT_CODE);
                }
            }
        }
    }

    @Override
    public boolean save(T entity) {
        resolveEntity(entity);
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntity);
        return super.saveBatch(entityList,batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        resolveEntity(entity);
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntity);
        return super.saveOrUpdateBatch(entityList,batchSize);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        entityList.forEach(this::resolveEntity);
        return super.updateBatchById(entityList);
    }

    @Override
    public boolean updateById(T entity) {
        resolveEntity(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        if (Fc.isNull(entity)){
            entity = BeanUtil.newInstance(ReflectUtil.getClassGenricType(this.getClass(),1));
        }
        resolveEntity(entity);
        return super.update(entity,updateWrapper);
    }

    /**
     * @author 郭丁志
     * @Description // 为结果查询字典
     * @date 23:49 2020/8/9 0009
     * @param result 查询结果
     * @param queryWrapper 查询条件
     */
    private void queryDict(Object result, Wrapper<T> queryWrapper){
        if (Fc.isNull(result) || (result instanceof List && ((List) result).size() <= 0) ){
            return;
        }

        List<Field> fields = DictSupport.getDictFiled(queryWrapper.getSqlSelect(),this.getClass());
        if(fields.size() > 0){
            List<Map<String,Object>> dicts = DictSupport.listDict(DictSupport.listDictName(fields));
            Map<String,List<Map<String,Object>>> dictMap =  DictSupport.listDictMap(dicts);

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
    public <V> List<V> listConver(Wrapper<T> queryWrapper, Function<T, V> function) {
        return list(queryWrapper).stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
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
        return ForestNodeMerger.merge(listConver(queryWrapper,t -> BeanUtil.copy(t, clz)));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据ID 真实删除
     * @Date 16:00 2020-08-11
     **/
    public boolean removeRealById(Serializable id) {
        return SqlHelper.retBool(getBaseMapper().deleteRealById(id));
    }

    /**
     * 根据 entity 条件，真实删除记录
     *
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    public boolean removeReal(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(getBaseMapper().deleteReal(queryWrapper));
    }

    /**
     * 真实删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    public boolean removeRealByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        return SqlHelper.retBool(getBaseMapper().deleteRealBatchIds(idList));
    }

    /**
     * 根据 columnMap 条件，真实删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    public boolean removeRealByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.retBool(getBaseMapper().deleteRealByMap(columnMap));
    }

}
