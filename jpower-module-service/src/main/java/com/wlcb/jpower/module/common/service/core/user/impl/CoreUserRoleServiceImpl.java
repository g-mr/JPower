package com.wlcb.jpower.module.common.service.core.user.impl;

import com.wlcb.jpower.module.common.service.core.user.CoreUserRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreUserRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
@Service("coreUserRoleService")
public class CoreUserRoleServiceImpl implements CoreUserRoleService {

    @Autowired
    public TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public List<Map<String,Object>> selectUserRoleByUserId(String userId) {
        return coreUserRoleDao.getBaseMapper().selectUserRoleByUserId(userId);
    }
}
