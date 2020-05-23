package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreUserService {

    /**
     * @Author 郭丁志
     * @Description //TODO 查询用户列表
     * @Date 17:30 2020-05-18
     * @Param [coreParam]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser>
     **/
    List<TbCoreUser> list(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增登录用户
     * @Date 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    Integer add(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量删除用户
     * @Date 11:28 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Integer delete(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改用户
     * @Date 11:36 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    Integer update(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 该用户名已存在
     * @Date 17:20 2020-05-19
     * @Param [loginId]
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    TbCoreUser selectUserLoginId(String loginId);
}
