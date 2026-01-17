package top.jpower.user.dbs.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.user.dbs.dao.mapper.TbCorePostMapper;
import top.jpower.user.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.jpower.vo.PostVo;

/**
 * @author mr.g
 * @date 2022-09-16 17:58
 */
@Repository
@RequiredArgsConstructor
public class TbCorePostDao extends JpowerServiceImpl<TbCorePostMapper, TbCorePost> implements BaseDaoWrapper<TbCorePost, PostVo> {

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
}
