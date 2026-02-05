package top.jpower.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.user.dbs.dao.CorePostDao;
import top.jpower.user.dbs.dao.TbCoreUserDao;
import top.jpower.user.dbs.dao.mapper.CorePostMapper;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.service.CorePostService;
import top.jpower.user.vo.PostVO;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CorePostServiceImpl extends BaseServiceImpl<CorePostMapper, CorePost> implements CorePostService {

    private final CorePostDao postDao;
    private final TbCoreUserDao userDao;

    @Override
    public Pg<PostVO> pageVo(Map<String, Object> map) {
        return postDao.pageVO(map);
    }

    @Override
    public boolean delete(List<Long> ids) {

        if (postDao.removeRealByIds(ids)){
            userDao.update(Wrappers.<TbCoreUser>lambdaUpdate()
                    .set(TbCoreUser::getPostId,null)
                    .in(TbCoreUser::getPostId,ids));
            return true;
        }
        return false;
    }
}
