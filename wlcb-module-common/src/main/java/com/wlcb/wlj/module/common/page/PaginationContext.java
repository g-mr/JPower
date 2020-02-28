package com.wlcb.wlj.module.common.page;


/**
 * @ClassName PaginationContext
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-14 23:27
 * @Version 1.0
 */
public class PaginationContext {
    // 定义两个threadLocal变量：pageNum和pageSize
    // 保存第几页
    private static ThreadLocal<Integer> pageNum = new ThreadLocal<Integer>();
    // 保存每页记录条数
    private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();

    /*
     * pageNum ：get、set、remove
     */
    public static int getPageNum() {
        Integer pn = pageNum.get();
        if (pn == null) {
            return 0;
        }
        return pn;
    }

    public static void setPageNum(int pageNumValue) {
        pageNum.set(pageNumValue);
    }

    public static void removePageNum() {
        pageNum.remove();
    }

    /*
     * pageSize ：get、set、remove
     */
    public static int getPageSize() {
        Integer ps = pageSize.get();
        if (ps == null) {
            return 0;
        }
        return ps;
    }

    public static void setPageSize(int pageSizeValue) {
        pageSize.set(pageSizeValue);
    }

    public static void removePageSize() {
        pageSize.remove();
    }
}
