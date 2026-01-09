package top.jpower.core.dbs.page;


import com.mybatisflex.core.constant.SqlConsts;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryOrderBy;
import lombok.Setter;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SqlUtil;
import top.jpower.core.util.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页上下文
 *
 * @author mr.g
 */
public class PaginationContext {
    /** 保存第几页 **/
    private static final ThreadLocal<Integer> pageNum = new ThreadLocal<Integer>();
    /** 保存每页记录条数 **/
    private static final ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
    /** 升序排序字段 **/
    private static final ThreadLocal<String> asc = new ThreadLocal<String>();
    /** 降序排序字段 **/
    private static final ThreadLocal<String> desc = new ThreadLocal<String>();

    @Setter
    private static Boolean optimizeCountQuery;

    /**
     * 获取分页
     *
     * @return 分页
     */
    public static <T> Page<T> page() {
        Page<T> page = Page.of(PaginationContext.getPageNum(), PaginationContext.getPageSize());
//        page.setTotalRow();
        page.setOptimizeCountQuery(optimizeCountQuery);
        return page;
    }

    /**
     * 获取排序
     *
     * @return 排序
     */
    public static List<QueryOrderBy> orderBy() {
        List<QueryOrderBy> orderBys = new ArrayList<>(2);
        Fc.toStrList(PaginationContext.getAsc()).forEach(asc -> orderBys.add(new QueryOrderBy(new QueryColumn(asc), SqlConsts.ASC)));
        Fc.toStrList(PaginationContext.getDesc()).forEach(desc -> orderBys.add(new QueryOrderBy(new QueryColumn(desc), SqlConsts.DESC)));
        return orderBys;
    }

    /**
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

    /**
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
