package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreRoleService {

    /**
     * @Author 郭丁志
     * @Description //TODO 查询角色列表
     * @Date 16:39 2020-05-19
     * @Param [coreRole]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole>
     **/
    List<TbCoreRole> listByParent(TbCoreRole coreRole);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增角色
     * @Date 16:55 2020-05-19
     * @Param [coreRole]
     * @return java.lang.Integer
     **/
    Integer add(TbCoreRole coreRole);

    /**
     * @Author 郭丁志
     * @Description //TODO 删除角色
     * @Date 16:58 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Integer deleteStatus(String ids);

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
    Integer update(TbCoreRole coreRole);
}
