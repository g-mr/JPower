package com.wlcb.jpower.module.dbs.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/11/22 0022 23:33
 */
public interface BaseDaoWrapper<T,V> {

    V conver(T user);

    default List<V> listConver(List<T> list){
        return list.stream().filter(Objects::nonNull).map(this::conver).collect(Collectors.toList());
    }

    default Page<V> pageConver(Page<T> userPage){
        List<V> list = listConver(userPage.getRecords());
        Page<V> pageVo = new Page<>(userPage.getCurrent(),userPage.getSize(),userPage.getTotal());
        pageVo.setRecords(list);
        return pageVo;
    }

}
