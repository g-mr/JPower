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

}
