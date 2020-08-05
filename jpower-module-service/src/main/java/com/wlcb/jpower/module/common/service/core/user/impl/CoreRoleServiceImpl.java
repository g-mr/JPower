package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
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
    public Boolean deleteStatus(String ids) {
        UpdateWrapper wrapper = new UpdateWrapper<TbCoreRole>();
        wrapper.in("id",ids.split(","));
        wrapper.set("status",0);
        return coreRoleDao.update(new TbCoreRole(),wrapper);
    }

    @Override
    public Integer listByPids(String ids) {
        QueryWrapper wrapper = new QueryWrapper<TbCoreRole>();

        wrapper.in("parent_id",ids);

        return coreRoleDao.count(wrapper);
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
