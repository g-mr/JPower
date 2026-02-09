package top.jpower.user.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.user.dbs.entity.CoreUserRole;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author mr.g
 **/
public interface CoreUserRoleService extends BaseService<CoreUserRole> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @author mr.g
     * @param userId 用户ID
     * @return 角色ID列表
     **/
    List<Long> queryRoleIds(Long userId);
}
