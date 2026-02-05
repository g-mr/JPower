package top.jpower.user.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.user.dbs.entity.CoreUserRole;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
public interface CoreUserRoleService extends BaseService<CoreUserRole> {

    /**
     * @author 郭丁志
     * @Description //TODO 查询用户角色ID
     * @date 0:04 2020/10/21 0021
     * @param userId
     */
    List<Long> queryRoleIds(Long userId);
}
