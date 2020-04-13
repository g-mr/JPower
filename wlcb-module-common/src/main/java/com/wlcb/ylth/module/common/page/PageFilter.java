package com.wlcb.ylth.module.common.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName PageFilter
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-14 23:31
 * @Version 1.0
 */
@Component
public class PageFilter implements Filter {
    public PageFilter() {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        PaginationContext.setPageNum(getPageNum(httpRequest));
        PaginationContext.setPageSize(getPageSize(httpRequest));
        PaginationContext.setOrderBy(getOrderBy(httpRequest));

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
            if (pageNums != null && StringUtils.isNumeric(pageNums)) {
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
            if (pageSizes != null && StringUtils.isNumeric(pageSizes)) {
                pageSize = Integer.parseInt(pageSizes);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取排序方式字段
     * @Date 15:43 2020-04-05
     * @Param [request]
     * @return int
     **/
    protected String getOrderBy(HttpServletRequest request) {
        // 默认每页10条记录
        String orderBy = null;
        try {
            String orderBys = request.getParameter("orderBy");
            if (StringUtils.isNotBlank(orderBys)) {
                orderBy = orderBys;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return orderBy;
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

}
