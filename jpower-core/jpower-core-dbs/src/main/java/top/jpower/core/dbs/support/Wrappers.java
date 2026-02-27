package top.jpower.core.dbs.support;

import cn.hutool.core.collection.ListUtil;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.LambdaGetter;
import top.jpower.core.dbs.page.PaginationContext;

import java.util.List;
import java.util.Map;

/**
 * 扩展QueryWrapper
 *
 * @author mr.g
 */
public class Wrappers {

    public static QueryWrapper getQueryWrapper() {
        QueryWrapper wrapper = QueryWrapper.create();
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }

    public static <T> QueryWrapper getQueryWrapper(T entity) {
        QueryWrapper wrapper = QueryWrapper.create(entity);
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }

    public static QueryWrapper getQueryWrapper(Map<String, Object> query) {
        List<String> exclude = ListUtil.of("pageNum", "pageSize", "asc", "desc", "tenantCode");
        return getQueryWrapper(query, exclude);
    }

    public static QueryWrapper getQueryWrapper(Map<String, Object> query, List<String> exclude) {
        exclude.forEach(query::remove);
        QueryWrapper qw = QueryWrapper.create();
        CPI.setOrderBys(qw, PaginationContext.orderBy());
        SqlWrapper.buildCondition(qw, query);
        return qw;
    }

    public static TreeWrapper getTreeWrapper(String id, String parentId) {
        TreeWrapper wrapper = new TreeWrapper(id,parentId);
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }

    public static <T> TreeWrapper getTreeWrapper(LambdaGetter<T> id, LambdaGetter<T> parentId) {
        TreeWrapper wrapper = new TreeWrapper(id, parentId);
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }
}
