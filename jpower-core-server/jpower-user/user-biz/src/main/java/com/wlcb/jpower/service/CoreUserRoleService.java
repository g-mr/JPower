package com.wlcb.jpower.service;

import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
public interface CoreUserRoleService extends BaseService<TbCoreUserRole> {

    /**
     * @author 郭丁志
     * @Description //TODO 查询用户角色ID
     * @date 0:04 2020/10/21 0021
     * @param userId
     */
    List<String> queryRoleIds(String userId);
}
