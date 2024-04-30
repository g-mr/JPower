package top.jpower.jpower.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.TbCoreUserRoleDao;
import top.jpower.jpower.dbs.dao.mapper.TbCoreUserRoleMapper;
import top.jpower.jpower.dbs.entity.TbCoreUserRole;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.CoreUserRoleService;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
@Service("coreUserRoleService")
public class CoreUserRoleServiceImpl extends BaseServiceImpl<TbCoreUserRoleMapper, TbCoreUserRole> implements CoreUserRoleService {

    @Autowired
    public TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public List<Long> queryRoleIds(Long userId) {
        return coreUserRoleDao.listObjs(Condition.<TbCoreUserRole>getQueryWrapper()
                .lambda().select(TbCoreUserRole::getRoleId).eq(TbCoreUserRole::getUserId,userId), Fc::toLong);
    }
}
