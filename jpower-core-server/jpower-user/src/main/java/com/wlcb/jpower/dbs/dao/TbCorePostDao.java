package com.wlcb.jpower.dbs.dao;

import com.wlcb.jpower.dbs.dao.mapper.TbCorePostMapper;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserMapper;
import com.wlcb.jpower.dbs.entity.TbCorePost;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.BaseDaoWrapper;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.vo.PostVo;
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
