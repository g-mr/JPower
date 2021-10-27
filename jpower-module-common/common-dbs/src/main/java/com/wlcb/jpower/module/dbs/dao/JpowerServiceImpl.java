package com.wlcb.jpower.module.dbs.dao;

import cn.hutool.core.bean.NullWrapperBean;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import com.wlcb.jpower.module.mp.support.Condition;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.tenant.TenantConstant.*;

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
    private void resolveEntity(T entity,boolean isSave){
        // todo start 这里获取实际登陆人，如果是匿名用户或者其他接口调用方，交给 mp来赋值（可应用于未登录状态，根据具体业务，接口调用方可控制这些字段来保存更加符合的值）
        String userId = SecureUtil.getUserId();
        String orgId = SecureUtil.getOrgId();
        userId = Fc.isBlank(userId)?null:userId;
        orgId = Fc.isBlank(orgId)?null:orgId;
        Date now = new Date();
        if (isSave){
            entity.setCreateTime(now);
            entity.setCreateUser(userId);
            entity.setCreateOrg(orgId);
            entity.setIsDeleted(JpowerConstants.DB_NOT_DELETE);
        }

        entity.setUpdateUser(userId);
        entity.setUpdateTime(now);
        // todo end

        Field field = cn.hutool.core.util.ReflectUtil.getField(entity.getClass(), TENANT_CODE);
        if (Fc.notNull(field)){
            String tenantCode = ReflectUtil.invokeGetter(entity,TENANT_CODE);
            if (SecureUtil.isRoot() && isSave){
                //如果是超级用户并且是保存数据，则必传一个租户编码
                ReflectUtil.invokeSetter(entity,TENANT_CODE,Fc.isBlank(tenantCode)?DEFAULT_TENANT_CODE:tenantCode);
            }else if (!SecureUtil.isRoot()){
                //如果不是超级用户，则不能传租户编码
                ReflectUtil.invokeSetter(entity,TENANT_CODE,new NullWrapperBean(String.class));
            }
        }
    }

    @Override
    public boolean save(T entity) {
        resolveEntity(entity,true);
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(e -> this.resolveEntity(e,true));
        return super.saveBatch(entityList,batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        resolveEntity(entity, Fc.isBlank(entity.getId()));
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(e -> this.resolveEntity(e,Fc.isBlank(e.getId())));
        return super.saveOrUpdateBatch(entityList,batchSize);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        entityList.forEach(e -> this.resolveEntity(e,false));
        return super.updateBatchById(entityList);
    }

    @Override
    public boolean updateById(T entity) {
        resolveEntity(entity,false);
        return super.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        if (Fc.isNull(entity)){
            entity = BeanUtil.newInstance(ReflectUtil.getClassGenricType(this.getClass(),1));
        }
        resolveEntity(entity,false);
        return super.update(entity,updateWrapper);
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
        return ForestNodeMerger.merge(listConver(queryWrapper,t -> BeanUtil.copyProperties(t, clz)));
    }

}
