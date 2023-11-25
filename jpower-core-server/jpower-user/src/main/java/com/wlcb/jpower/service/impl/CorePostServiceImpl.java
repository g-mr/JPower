package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.dao.TbCorePostDao;
import com.wlcb.jpower.dbs.dao.TbCoreUserDao;
import com.wlcb.jpower.dbs.dao.mapper.TbCorePostMapper;
import com.wlcb.jpower.dbs.entity.TbCorePost;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MapUtil;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.CorePostService;
import com.wlcb.jpower.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.tenant.TenantConstant.TENANT_CODE;

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
