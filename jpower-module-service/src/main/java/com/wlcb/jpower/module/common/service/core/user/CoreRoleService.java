package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;

import java.util.List;

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
     * @Description //TODO 删除角色
     * @Date 16:58 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Boolean deleteStatus(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 根据批量id查询下级角色数量
     * @Date 17:10 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Integer listByPids(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询角色
     * @Date 17:15 2020-05-19
     * @Param [code]
     * @return com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole
     **/
    TbCoreRole selectRoleByCode(String code);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改角色信息
     * @Date 17:27 2020-05-19
     * @Param [coreRole]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreRole coreRole);
}
