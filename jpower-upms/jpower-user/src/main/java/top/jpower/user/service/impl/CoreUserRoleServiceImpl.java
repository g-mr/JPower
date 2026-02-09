package top.jpower.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.user.dbs.dao.CoreUserRoleDao;
import top.jpower.user.dbs.dao.mapper.CoreUserRoleMapper;
import top.jpower.user.dbs.entity.CoreUserRole;
import top.jpower.user.service.CoreUserRoleService;

import java.util.List;

/**
 * 用户角色
 *
 * @author mr.g
 **/
@Service
@RequiredArgsConstructor
public class CoreUserRoleServiceImpl extends BaseServiceImpl<CoreUserRoleMapper, CoreUserRole> implements CoreUserRoleService {

    public final CoreUserRoleDao coreUserRoleDao;

    @Override
    public List<Long> queryRoleIds(Long userId) {
        return coreUserRoleDao.queryRoleIds(userId);
    }
}
