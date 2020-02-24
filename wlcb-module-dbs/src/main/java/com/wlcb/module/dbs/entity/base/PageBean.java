package com.wlcb.module.dbs.entity.base;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageBean
 * @Description TODO
 * @Author 郭丁志 分页信息
 * @Date 2020-02-14 23:24
 * @Version 1.0
 */
@Data
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = 8656597559014685635L;

    //总记录数
    private long total;
    //结果集
    private List<T> list;
    // 第几页
    private int pageNum;
    // 每页记录数
    private int pageSize;
    // 总页数
    private int pages;
    // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
    private int size;

    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     * @param list          page结果
     */
    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
        }
    }

}
