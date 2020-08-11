package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreRoleService")
public class CoreRoleServiceImpl extends BaseServiceImpl<TbCoreRoleMapper,TbCoreRole> implements CoreRoleService {

    @Autowired
    private TbCoreRoleDao coreRoleDao;

    @Override
    public Boolean add(TbCoreRole coreRole) {
        return coreRoleDao.save(coreRole);
    }

    @Override
    public Integer listByPids(String ids) {
        return coreRoleDao.count(Condition.<TbCoreRole>getQueryWrapper().lambda().in(TbCoreRole::getParentCode, Fc.toStrList(ids)));
    }

    @Override
    public TbCoreRole selectRoleByCode(String code) {
        return coreRoleDao.getOne(new QueryWrapper<TbCoreRole>().lambda().eq(TbCoreRole::getCode,code));
    }

    @Override
    public Boolean update(TbCoreRole coreRole) {
        return coreRoleDao.updateById(coreRole);
    }
}
