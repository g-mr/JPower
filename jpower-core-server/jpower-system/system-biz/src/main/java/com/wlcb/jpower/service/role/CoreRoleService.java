package com.wlcb.jpower.service.role;

import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.module.common.service.BaseService;

/**
 * @author mr.gmac
 */
public interface CoreRoleService extends BaseService<TbCoreRole> {

    /**
     * @Author 郭丁志
     * @Description //TODO 新增角色
     * @Date 16:55 2020-05-19
     * @Param [coreRole]
     * @return java.lang.Integer
     **/
    Boolean add(TbCoreRole coreRole);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据批量id查询下级角色数量
     * @Date 17:10 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    long listByPids(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改角色信息
     * @Date 17:27 2020-05-19
     * @Param [coreRole]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreRole coreRole);
}
