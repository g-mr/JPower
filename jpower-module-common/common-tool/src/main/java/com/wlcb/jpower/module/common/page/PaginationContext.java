package com.wlcb.jpower.module.common.page;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SqlUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName PaginationContext
 * @Description TODO 分页
 * @Author 郭丁志
 * @Date 2020-02-14 23:27
 * @Version 1.0
 */
public class PaginationContext {
    /** 保存第几页 **/
    private static ThreadLocal<Integer> pageNum = new ThreadLocal<Integer>();
    /** 保存每页记录条数 **/
    private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
    /** 升序排序字段 **/
    private static ThreadLocal<String> asc = new ThreadLocal<String>();
    /** 降序排序字段 **/
    private static ThreadLocal<String> desc = new ThreadLocal<String>();

    /**
     * @author 郭丁志
     * @Description //TODO 分页工具分页
     * @date 1:13 2020/8/9 0009
     */
    public static void startPage() {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());

        String asc = StringUtil.removeAllPrefixAndSuffixLowerFirst(Fc.toStr(PaginationContext.getAsc()),StringPool.COMMA);
        String desc = StringUtil.removeAllPrefixAndSuffixLowerFirst(Fc.toStr(PaginationContext.getDesc()),StringPool.COMMA);

        if (Fc.isNotBlank(desc)){
            desc = StringUtil.replace(desc,StringPool.COMMA,StringPool.SPACE+StringPool.DESC+StringPool.COMMA)+StringPool.SPACE+StringPool.DESC;
        }

        if (Fc.isNotBlank(asc)){
            asc = StringUtil.replace(asc,StringPool.COMMA,StringPool.SPACE+StringPool.ASC+StringPool.COMMA)+StringPool.SPACE+StringPool.ASC;
        }

        String orderBy = StringUtil.removeAllPrefixAndSuffix(asc + StringPool.COMMA + desc,StringPool.COMMA);

        if (Fc.isNotBlank(orderBy)){
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 获取MP的分页类
     * @date 1:14 2020/8/9 0009
     */
    public static <T> Page<T> getMpPage() {
        Page<T> page = new Page<T>(PaginationContext.getPageNum(),PaginationContext.getPageSize());

        String asc = PaginationContext.getAsc();
        if (StringUtils.isNotBlank(asc)){
            page.addOrder(OrderItem.ascs(Fc.toStrArray(asc)));
        }

        String desc = PaginationContext.getDesc();
        if (StringUtils.isNotBlank(desc)){
            page.addOrder(OrderItem.descs(Fc.toStrArray(desc)));
        }
        return page;
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

    public static String getAsc() {
        if (Fc.isNotBlank(StringUtil.humpToUnderline(SqlUtil.escapeOrderBySql(asc.get())))){
            return StringUtil.humpToUnderline(SqlUtil.escapeOrderBySql(asc.get()));
        }
        return "";
    }

    public static void setAsc(String ascValue) {
        asc.set(ascValue);
    }

    public static String getDesc() {
        if (Fc.isNotBlank(StringUtil.humpToUnderline(SqlUtil.escapeOrderBySql(desc.get())))){
            return StringUtil.humpToUnderline(SqlUtil.escapeOrderBySql(desc.get()));
        }
        return "";
    }

    public static void setDesc(String descValue) {
        desc.set(descValue);
    }

    public static void removeOrderBy() {
        asc.remove();
        desc.remove();
    }
}
