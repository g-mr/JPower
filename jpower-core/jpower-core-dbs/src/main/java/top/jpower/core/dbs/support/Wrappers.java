package top.jpower.core.dbs.support;

import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.LambdaGetter;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.ChainMap;

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

    public static <T> QueryWrapper getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        Map<String,Object> exclude = ChainMap.<String,Object>create().put("pageNum", "pageNum").put("pageSize", "pageSize").put("asc", "asc").put("desc", "desc").put("tenantCode", "tenantCode").build();
        return getQueryWrapper(query, exclude, clazz);
    }

    public static <T> QueryWrapper getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
        exclude.forEach((k, v) -> {
            query.remove(k);
        });
        QueryWrapper qw = QueryWrapper.create(BeanUtil.newBean(clazz));
        CPI.setOrderBys(qw, PaginationContext.orderBy());
        SqlWrapper.buildCondition(qw, query);
        return qw;
    }

    public static TreeWrapper getTreeWrapper(String id, String parentId) {
        TreeWrapper wrapper = new TreeWrapper(id,parentId);
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }

    public static <T> TreeWrapper getLambdaTreeWrapper(LambdaGetter<T> id, LambdaGetter<T> parentId) {
        TreeWrapper wrapper = new TreeWrapper(id, parentId);
        CPI.setOrderBys(wrapper, PaginationContext.orderBy());
        return wrapper;
    }
}
