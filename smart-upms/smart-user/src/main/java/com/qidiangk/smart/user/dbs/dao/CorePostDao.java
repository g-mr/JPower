package com.qidiangk.smart.user.dbs.dao;

import com.mybatisflex.core.query.QueryMethods;
import org.springframework.stereotype.Repository;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.user.dbs.dao.mapper.CorePostMapper;
import com.qidiangk.smart.user.dbs.entity.CorePost;
import com.qidiangk.smart.user.dbs.entity.CoreUser;
import com.qidiangk.smart.user.pojo.PostSelectVO;
import com.qidiangk.smart.user.pojo.PostVO;

import java.util.List;
import java.util.Map;

import static com.qidiangk.smart.user.dbs.entity.table.CorePostTableDef.CORE_POST;

/**
 * 岗位SQL
 *
 * @author mr.g
 */
@Repository
public class CorePostDao extends JpowerServiceImpl<CorePostMapper, CorePost> implements BaseDaoWrapper<CorePost> {

    /**
     * 查询分页
     *
     * @author mr.g
     * @param map 查询条件
     * @return 数据
     **/
    public Pg<PostVO> pageVO(Map<String, Object> map) {
        return getMapper().pageAs(PaginationContext.page(),
                    Wrappers.getQueryWrapper(map)
                            .select(CORE_POST.DEFAULT_COLUMNS)
                            .select(QueryMethods.count(CoreUser::getId).as(PostVO::getUserNum))
                            .leftJoin(CoreUser.class).on(CorePost::getId, CoreUser::getPostId)
                            .groupBy(CorePost::getId)
                            .orderBy(CorePost::getSort).asc(),
                PostVO.class);
    }

    public List<PostSelectVO> listSelect(String name) {
        return super.listAs(Wrappers.getQueryWrapper()
                .select(CorePost::getId, CorePost::getName, CorePost::getCode)
                .eq(CorePost::getStatus, YN01Enum.Y.getValue())
                .like(CorePost::getName, name, Fc.isNoneBlank(name))
                .orderBy(CorePost::getSort, Boolean.TRUE), PostSelectVO.class);
    }
}
