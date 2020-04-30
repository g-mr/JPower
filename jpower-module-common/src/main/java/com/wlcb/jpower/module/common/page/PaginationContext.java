package com.wlcb.jpower.module.common.page;


import com.github.pagehelper.PageHelper;
import com.wlcb.jpower.module.common.utils.sql.SqlUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName PaginationContext
 * @Description TODO 分页
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
    // 排序字段
    private static ThreadLocal<String> orderBy = new ThreadLocal<String>();

    public static void startPage()
    {
        Integer pageNum = PaginationContext.getPageNum();
        Integer pageSize = PaginationContext.getPageSize();
        if (pageNum != null && pageSize != null){
            String orderBy = SqlUtil.escapeOrderBySql(PaginationContext.getOrderBy());
            if (StringUtils.isNotBlank(orderBy)){
                PageHelper.startPage(pageNum, pageSize, orderBy);
            }else {
                PageHelper.startPage(pageNum, pageSize);
            }
        }
    }

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

    public static String getOrderBy() {
        return orderBy.get();
    }

    public static void setOrderBy(String orderByValue) {
        orderBy.set(orderByValue);
    }

    public static void removeOrderBy() {
        orderBy.remove();
    }
}
