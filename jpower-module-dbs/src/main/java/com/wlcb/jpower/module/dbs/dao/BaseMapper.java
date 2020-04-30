package com.wlcb.jpower.module.dbs.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T> {

    int deleteById(String id);

    int insert(T t);

    int insertSelective(T t);

    int updateByPrimaryKey(T t);

    int updateByPrimaryKeySelective(T t);

    T selectById(@Param("id") String id);

    List<T> listAll(T t);
}
