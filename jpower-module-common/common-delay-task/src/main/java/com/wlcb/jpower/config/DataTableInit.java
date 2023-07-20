package com.wlcb.jpower.config;

import cn.hutool.core.collection.CollUtil;
import com.wlcb.jpower.annotation.JEntity;
import com.wlcb.jpower.annotation.JId;
import com.wlcb.jpower.module.common.utils.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 数据库表初始化
 *
 * @author mr.g
 * @date 2023/7/2 11:40 AM
 */
@Component
public class DataTableInit implements InitializingBean {

    @Resource
    private JdbcTemplate taskJdbcTemplate;

    @Override
    public void afterPropertiesSet() {
        //实现数据库初始化
        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation("com.wlcb.jpower.task.entity", JEntity.class);
        if (Fc.isNotEmpty(classSet)){
            List<String> tableNames = taskJdbcTemplate.queryForList("SHOW TABLES", String.class);
            classSet.forEach(clz->{
                if (BeanUtil.isBean(clz)){
                    String clzName = StringUtil.humpToUnderline(ClassUtil.getClassName(clz,true));
                    if (!CollUtil.contains(tableNames, name -> StringUtil.equalsIgnoreCase(name,clzName) )){
                        JEntity entity = AnnotationUtil.getAnnotation(clz, JEntity.class);
                        Field field = CollUtil.getFirst(AnnotationUtil.findAnnotatedFields(clz, JId.class));
                        taskJdbcTemplate.execute(Objects.requireNonNull(SqlUtil.buildCreateMysqlTable(clz, field, entity.value()), "生成create table失败"));
                    }
                }
            });
        }
    }
}
