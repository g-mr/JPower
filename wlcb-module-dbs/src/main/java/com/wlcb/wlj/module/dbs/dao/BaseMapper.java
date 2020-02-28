package com.wlcb.wlj.module.dbs.dao;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T> {

    int deleteById(String id);

    int inster(T t);

    int insterSelective(T t);

    int updateByPrimaryKey(T t);

    int updateByPrimaryKeySelective(T t);

    T selectById(@Param("id") String id);
}
