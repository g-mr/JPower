package top.jpower.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jpower.core.util.utils.Fc;
import top.jpower.user.dbs.dao.TbCoreUserRoleDao;
import top.jpower.user.dbs.dao.mapper.TbCoreUserRoleMapper;
import top.jpower.jpower.dbs.entity.TbCoreUserRole;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.user.service.CoreUserRoleService;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
@Service
public class CoreUserRoleServiceImpl extends BaseServiceImpl<TbCoreUserRoleMapper, TbCoreUserRole> implements CoreUserRoleService {

    @Autowired
    public TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public List<Long> queryRoleIds(Long userId) {
        return coreUserRoleDao.listObjs(Condition.<TbCoreUserRole>getQueryWrapper()
                .lambda().select(TbCoreUserRole::getRoleId).eq(TbCoreUserRole::getUserId,userId), Fc::toLong);
    }
}
