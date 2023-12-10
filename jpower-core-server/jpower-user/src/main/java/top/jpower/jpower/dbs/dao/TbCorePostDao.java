package top.jpower.jpower.dbs.dao;

import top.jpower.jpower.dbs.dao.mapper.TbCorePostMapper;
import top.jpower.jpower.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.common.utils.BeanUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.dbs.dao.BaseDaoWrapper;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
