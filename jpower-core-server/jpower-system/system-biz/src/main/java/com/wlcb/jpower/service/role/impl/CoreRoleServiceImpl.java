package com.wlcb.jpower.service.role.impl;

import com.wlcb.jpower.dbs.dao.role.TbCoreRoleDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreRoleMapper;
import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreRoleService")
public class CoreRoleServiceImpl extends BaseServiceImpl<TbCoreRoleMapper, TbCoreRole> implements CoreRoleService {

    @Autowired
    private TbCoreRoleDao coreRoleDao;

    @Override
    public Boolean add(TbCoreRole coreRole) {
        return coreRoleDao.save(coreRole);
    }

    @Override
    public long listByPids(String ids) {
        return coreRoleDao.count(Condition.<TbCoreRole>getQueryWrapper().lambda().in(TbCoreRole::getParentId, Fc.toStrList(ids)));
    }

    @Override
    public Boolean update(TbCoreRole coreRole) {
        return coreRoleDao.updateById(coreRole);
    }
}
