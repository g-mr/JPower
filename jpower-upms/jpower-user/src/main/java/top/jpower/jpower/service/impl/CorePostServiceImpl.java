package top.jpower.jpower.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.MapUtil;
import top.jpower.jpower.dbs.dao.TbCorePostDao;
import top.jpower.jpower.dbs.dao.TbCoreUserDao;
import top.jpower.jpower.dbs.dao.mapper.TbCorePostMapper;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.CorePostService;
import top.jpower.jpower.vo.PostVo;

import java.util.List;
import java.util.Map;

import static top.jpower.jpower.module.tenant.TenantConstant.TENANT_CODE;

/**
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CorePostServiceImpl extends BaseServiceImpl<TbCorePostMapper, TbCorePost> implements CorePostService {

    private final TbCorePostDao postDao;
    private final TbCoreUserDao userDao;

    @Override
    public Page<PostVo> pageVo(Map<String, Object> map) {
        String tenant = MapUtil.getStr(map,TENANT_CODE);
        return postDao.pageConver(postDao.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbCorePost.class)
                        .lambda()
                        .eq(ShieldUtil.isRoot() && Fc.isNotBlank(tenant),TbCorePost::getTenantCode, tenant)
                        .orderByAsc(TbCorePost::getSort)));
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
