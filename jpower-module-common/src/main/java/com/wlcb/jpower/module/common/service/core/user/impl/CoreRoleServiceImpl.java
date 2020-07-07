package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreRoleService")
public class CoreRoleServiceImpl implements CoreRoleService {

    @Autowired
    private TbCoreRoleMapper coreRoleMapper;
    @Autowired
    private TbCoreRoleDao coreRoleDao;

    @Override
    public List<TbCoreRole> listByParent(TbCoreRole coreRole) {
        QueryWrapper wrapper = new QueryWrapper<TbCoreRole>();

        if (StringUtils.isNotBlank(coreRole.getCode())){
            wrapper.eq("code",coreRole.getCode());
        }

        if (StringUtils.isNotBlank(coreRole.getName())){
            wrapper.eq("name",coreRole.getName());
        }

        if (StringUtils.isNotBlank(coreRole.getParentId())){
            wrapper.eq("parent_id",coreRole.getParentId());
        }else {
            wrapper.isNull("parent_id");
        }

        if (StringUtils.isNotBlank(coreRole.getParentCode())){
            wrapper.eq("parent_code",coreRole.getParentCode());
        }else {
            wrapper.isNull("parent_code");
        }

        if (coreRole.getIsSysRole() != null){
            wrapper.eq("is_sys_role",coreRole.getIsSysRole());
        }

        wrapper.eq("status",1);

        wrapper.orderByAsc("sort");

        return coreRoleMapper.selectList(wrapper);
    }

    @Override
    public Integer add(TbCoreRole coreRole) {
        coreRole.setUpdateUser(coreRole.getCreateUser());
        return coreRoleDao.save(coreRole)?1:0;
    }

    @Override
    public Integer deleteStatus(String ids) {
        UpdateWrapper wrapper = new UpdateWrapper<TbCoreRole>();
        wrapper.in("id",ids);
        wrapper.set("status",0);
        return coreRoleDao.update(new TbCoreRole(),wrapper)?1:0;
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
    public Integer update(TbCoreRole coreRole) {
        return coreRoleDao.updateById(coreRole)?1:0;
    }
}
