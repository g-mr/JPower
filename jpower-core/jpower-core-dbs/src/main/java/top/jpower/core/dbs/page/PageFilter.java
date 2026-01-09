package top.jpower.core.dbs.page;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import top.jpower.core.dbs.config.properties.MybatisProperties;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;

/**
 * 分页过滤器
 *
 * @author mr.g
 */
@Order(10)
@RequiredArgsConstructor
public class PageFilter implements Filter {

    private final MybatisProperties.Page page;

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        PaginationContext.setOptimizeCountQuery(page.isOptimizeCountQuery());

        PaginationContext.setPageNum(getPageNum(httpRequest));
        PaginationContext.setPageSize(getPageSize(httpRequest));
        PaginationContext.setAsc(getAsc(httpRequest));
        PaginationContext.setDesc(getDesc(httpRequest));

        try {
            chain.doFilter(request, response);
        }
        // 使用完Threadlocal，将其删除。使用finally确保一定将其删除
        finally {
            PaginationContext.removePageNum();
            PaginationContext.removePageSize();
            PaginationContext.removeOrderBy();

        }
    }

    /**
     * 获得pager.offset参数的值
     *
     * @param request
     * @return
     */
    protected int getPageNum(HttpServletRequest request) {
        int pageNum = 1;
        try {
            String pageNums = request.getParameter("pageNum");
            if (StrUtil.isNumeric(pageNums)) {
                pageNum = Integer.parseInt(pageNums);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageNum;
    }

    /**
     * 设置默认每页大小
     *
     * @return
     */
    protected int getPageSize(HttpServletRequest request) {
        // 默认每页10条记录
        int pageSize = 10;
        try {
            String pageSizes = request.getParameter("pageSize");
            if (StrUtil.isNumeric(pageSizes)) {
                pageSize = Integer.parseInt(pageSizes);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    /**
     * 获取升序排序方式字段
     *
     * @param request
     * @return
     */
    protected String getAsc(HttpServletRequest request) {
        String asc = null;
        String ascs = request.getParameter("asc");
        if (Fc.isNotBlank(ascs)) {
            asc = ascs;
        }
        return asc;
    }

    /**
     * 获取降序排序方式字段
     *
     * @param request
     * @return
     */
    protected String getDesc(HttpServletRequest request) {
        String desc = null;
        String descs = request.getParameter("desc");
        if (Fc.isNotBlank(descs)) {
            desc = descs;
        }
        return desc;
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

}
