package top.jpower.user.service.impl;

import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import top.jpower.user.dbs.dao.CorePostDao;
import top.jpower.user.dbs.dao.CoreUserDao;
import top.jpower.user.dbs.dao.mapper.CorePostMapper;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.service.CorePostService;
import top.jpower.user.vo.PostSelectVO;
import top.jpower.user.vo.PostVO;

import java.util.List;
import java.util.Map;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;

/**
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CorePostServiceImpl extends BaseServiceImpl<CorePostMapper, CorePost> implements CorePostService {

    private final CorePostDao postDao;
    private final CoreUserDao userDao;

    @Override
    public Pg<PostVO> pageVo(Map<String, Object> map) {
        return postDao.pageVO(map);
    }

    @Override
    public boolean deleteInIds(List<Long> ids) {
        if (postDao.removeRealByIds(ids)){
            userDao.update(UpdateEntity.of(CoreUser.class).setPostId(null), Wrappers.getQueryWrapper().in(CoreUser::getPostId, ids));
            CacheUtil.clear(CacheNames.POST_KEY);
            return true;
        }
        return false;
    }

    @Override
    public List<PostSelectVO> listSelect(String name) {
        return postDao.listSelect(name);
    }

    @Override
    public Long createPost(CorePost corePost) {
        JpowerAssert.geZero(postDao.count(Wrappers.getQueryWrapper().eq(CorePost::getCode, corePost.getCode())), JpowerError.Arg, CODE_EXIST);
        postDao.save(corePost);
        return corePost.getId();
    }

    @Override
    public Long editById(CorePost corePost) {
        CorePost post = postDao.getOneByField(CorePost::getCode, corePost.getCode());
        JpowerAssert.notTrue(Fc.notNull(post) && Fc.notEqualsValue(post.getId(),corePost.getId()),JpowerError.Arg,CODE_EXIST);

        if (postDao.updateById(UpdateEntity.ofNotNull(corePost)
                .setDescribe(corePost.getDescribe())
                .setCondition(corePost.getCondition()))) {
            CacheUtil.clear(CacheNames.POST_KEY);
        }
        return corePost.getId();
    }
}
