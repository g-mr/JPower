package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleMapper;
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

    @Override
    public List<TbCoreRole> listByParent(TbCoreRole coreRole) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreRole>();

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

        wrapper.orderBy("sort",true);

        return coreRoleMapper.selectList(wrapper);
    }

    @Override
    public Integer add(TbCoreRole coreRole) {
        coreRole.setUpdateUser(coreRole.getCreateUser());
        return coreRoleMapper.insert(coreRole);
    }

    @Override
    public Integer deleteStatus(String ids) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreRole>();
        wrapper.in("id",ids);
        return coreRoleMapper.updateForSet("status=0",wrapper);
    }

    @Override
    public Integer listByPids(String ids) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreRole>();

        wrapper.in("parent_id",ids);

        return coreRoleMapper.selectCount(wrapper);
    }

    @Override
    public TbCoreRole selectRoleByCode(String code) {
        TbCoreRole coreRole = new TbCoreRole();
        coreRole.setCode(code);
        return coreRoleMapper.selectOne(coreRole);
    }

    @Override
    public Integer update(TbCoreRole coreRole) {
        return coreRoleMapper.updateById(coreRole);
    }
}
