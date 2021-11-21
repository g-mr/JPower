package com.wlcb.jpower.module.dynatable.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

/**
 * 动态表名实现类
 * @author mr.g
 * @date 2021-11-21 19:26
 */
public class DynamicTableNameHandler implements TableNameHandler {


    @Override
    public String dynamicTableName(String sql, String tableName) {
        // TODO: 2021-11-21 后续版本实现，考虑可以使用自定义bean注解
        // TODO: 2021-11-21  添加自定义bean注解如何实现自定义逻辑？？？
        return tableName;
    }


}
