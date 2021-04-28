package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BaseEntityWrapper
 * @Description TODO entity转换基础实现
 * @Author 郭丁志
 * @Date 2020-10-16 15:37
 * @Version 1.0
 */
public abstract class BaseWrapper <T, V> {

    protected abstract V conver(T entity);

    public V entityVO(T entity){
        return conver(entity);
    }

    public List<V> listVO(List<T> list) {
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }

    /**
     * 把mp分页组件转换
     *
     * @author 郭丁志
     * @date 0:13 2021/2/14 0014
     * @param pages
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<V>
     */
    public Page<V> pageVo(Page<T> pages){
        List<V> list = this.listVO(pages.getRecords());
        Page<V> pageVo = new Page<>(pages.getCurrent(),pages.getSize(),pages.getTotal());
        pageVo.setRecords(list);
        return pageVo;
    }

    /**
     * 分页组件转换
     *
     * @author 郭丁志
     * @date 0:13 2021/2/14 0014
     * @param list
     * @return com.github.pagehelper.PageInfo<V>
     */
    public PageInfo<V> pageVo(List<T> list){
        PageInfo<T> pageInfo = new PageInfo<>(list);
        com.github.pagehelper.Page<V> page = new com.github.pagehelper.Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        page.setTotal(pageInfo.getTotal());
        list.forEach((t)->page.add(entityVO(t)));
        return new PageInfo<>(page, pageInfo.getNavigatePages());
    }

}
