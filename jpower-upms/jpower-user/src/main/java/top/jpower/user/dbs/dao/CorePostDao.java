package top.jpower.user.dbs.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.vo.PostVo;
import top.jpower.user.dbs.dao.mapper.CorePostMapper;
import top.jpower.user.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.vo.PostVO;

import java.util.Map;

/**
 * @author mr.g
 * @date 2022-09-16 17:58
 */
@Repository
@RequiredArgsConstructor
public class CorePostDao extends JpowerServiceImpl<CorePostMapper, CorePost> implements BaseDaoWrapper<CorePost> {

    private final TbCoreUserMapper userMapper;

    @Override
    public PostVo conver(TbCorePost post) {

        if (Fc.notNull(post)) {
            PostVo postVo = BeanUtil.copyProperties(post,PostVo.class);
            postVo.setUserNum(userMapper.selectCount(Condition.<TbCoreUser>getQueryWrapper()
                    .lambda().eq(TbCoreUser::getPostId,post.getId())));
            return postVo;
        }

        return null;
    }

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
                    .orderBy(CorePost::getSort).asc(),
                PostVO.class);
    }
}
