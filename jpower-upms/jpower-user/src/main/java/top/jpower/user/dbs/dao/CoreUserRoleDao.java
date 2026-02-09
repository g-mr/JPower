package top.jpower.user.dbs.dao;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.user.dbs.dao.mapper.CoreUserRoleMapper;
import top.jpower.user.dbs.entity.CoreUserRole;

import java.util.List;

/**
 * @author mr.gmac
 */
@Repository
public class CoreUserRoleDao extends JpowerServiceImpl<CoreUserRoleMapper, CoreUserRole> {

    /**
     * 批量删除用户的角色关系
     *
     * @author mr.g
     * @param userIds 用户ID列表
     * @return boolean 是否删除成功
     **/
    public boolean deleteByUserIds(List<Long> userIds) {
        return super.removeReal(Wrappers.getQueryWrapper().in(CoreUserRole::getUserId, userIds));
    }

    /**
     * 根据角色ID和用户ID列表删除用户角色关系
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     * @return boolean 是否删除成功
     **/
    public boolean deleteByRoleAndUserIds(Long roleId, List<Long> userIds) {
        return super.removeReal(Wrappers.getQueryWrapper().eq(CoreUserRole::getRoleId, roleId).in(CoreUserRole::getUserId, userIds));
    }

    /**
     * 根据用户ID查询角色ID列表
     *
     * @author mr.g
     * @param userId 用户ID
     * @return 角色ID列表
     **/
    public List<Long> queryRoleIds(Long userId) {
        return super.objListAs(Wrappers.getQueryWrapper()
                .select(CoreUserRole::getRoleId)
                .eq(CoreUserRole::getUserId, userId), Long.TYPE);
    }
}
