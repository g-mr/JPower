package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.dbs.dao.TbCoreUserRoleDao;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserRoleMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.CoreUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
@Service("coreUserRoleService")
public class CoreUserRoleServiceImpl extends BaseServiceImpl<TbCoreUserRoleMapper, TbCoreUserRole> implements CoreUserRoleService {

    @Autowired
    public TbCoreUserRoleDao coreUserRoleDao;

}
