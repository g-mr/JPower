package com.wlcb.jpower.module.datascope;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.MSUtils;
import com.wlcb.jpower.module.common.utils.ClassUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.datascope.handler.DataScopeHandler;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author mr.g
 * @date 2021-04-23 17:31
 */
public class JpowerPageHelper extends PageHelper {

    private DataScopeHandler dataScopeHandler;

    /**
     * pagehelper分页数据权限sql拦截
     * @author mr.g
     */
    @Override
    public String getCountSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey countKey) {
        if (!skip(ms)){
            String sqlCondition = dataScopeHandler.sql(ms.getResultMaps().get(0).getId(),boundSql.getSql());
            if (Fc.isNotBlank(sqlCondition)){
                PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
                mpBoundSql.sql(sqlCondition);
            }
        }
        return super.getCountSql(ms,boundSql,parameterObject,rowBounds,countKey);
    }

    private boolean skip(MappedStatement ms){
        String msId = ms.getResultMaps().get(0).getId();
        return !(Fc.isNotBlank(msId) &&
                ms.getId().startsWith(msId) &&
                ms.getId().endsWith(MSUtils.COUNT) &&
                Fc.equals(ClassUtil.getClassName(ms.getResultMaps().get(0).getType(),false),ClassUtil.getClassName(Long.class,false)) &&
                !SecureUtil.isRoot()
                && Fc.notNull(SecureUtil.getUser())
                );
    }

    @Override
    public void setProperties(Properties properties) {
        dataScopeHandler = SpringUtil.getBean("dataScopeHandler");
        super.setProperties(properties);
    }

}
