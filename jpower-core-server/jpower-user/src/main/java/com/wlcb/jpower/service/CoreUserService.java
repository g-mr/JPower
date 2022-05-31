package com.wlcb.jpower.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.vo.UserVo;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreUserService extends BaseService<TbCoreUser> {

    /**
     * @Author 郭丁志
     * @Description //TODO 查询用户列表
     * @Date 17:30 2020-05-18
     * @Param [coreParam]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser>
     **/
    PageInfo<UserVo> listPage(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增登录用户
     * @Date 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    @Override
    boolean save(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量删除用户
     * @Date 11:28 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Boolean delete(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改用户
     * @Date 11:36 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    Boolean update(TbCoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 该用户名已存在
     * @Date 17:20 2020-05-19
     * @Param [loginId]
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    TbCoreUser selectUserLoginId(String loginId,String tenantCode);

    /**
     * @author 郭丁志
     * @Description //TODO 通过id查询用户信息
     * @date 1:27 2020/5/24 0024
     * @param id 用户id
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser
     */
    UserVo selectUserById(String id);

    /**
     * @author 郭丁志
     * @Description //TODO 修改用户密码
     * @date 1:28 2020/5/24 0024
     * @param ids 用户id
     * @param pass 用户加密后密码
     * @return java.lang.Integer
     */
    Boolean updateUserPassword(String ids, String pass);

    /**
     * @author 郭丁志
     * @Description //TODO 批量新增
     * @date 2:55 2020/5/24 0024
     * @param list
     * @return java.lang.Integer
     */
    boolean insertBatch(List<TbCoreUser> list,boolean isCover);

    /**
     * @author 郭丁志
     * @Description //TODO 更新用户角色
     * @date 0:44 2020/5/25 0025
     * @param userIds 用户ID 多个逗号分隔
     * @param roleIds  角色ID 多个逗号分隔
     * @return java.lang.Integer
     */
    Boolean updateUsersRole(String userIds, String roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过手机号查找用户
     * @Date 10:38 2020-07-03
     * @Param [phone]
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    TbCoreUser selectByPhone(String phone,String tenantCode);

    /**
     * @author 郭丁志
     * @Description //TODO 更新用户登陆信息
     * @date 0:02 2020/10/21 0021
     * @param user
     */
    Boolean updateLoginInfo(TbCoreUser user);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询用户列表
     * @Date 14:49 2020-08-20
     * @Param [coreUser, orgCode]
     **/
    List<UserVo> list(TbCoreUser coreUser);

    /**
     * @author 郭丁志
     * @Description //TODO 通过第三方验证码查询用户
     * @date 0:08 2020/10/21 0021
     * @param otherCode
     * @param tenantCode
     */
    TbCoreUser selectUserByOtherCode(String otherCode, String tenantCode);

    /**
     * @author 郭丁志
     * @Description // 创建管理员用户
     * @date 0:09 2020/10/25 0025
     * @param user 用户信息
     * @param roleId 角色ID
     */
    boolean saveAdmin(TbCoreUser user, String roleId);

    boolean addRoleUsers(String roleId, List<String> userIds);

    boolean deleteRoleUsers(String roleId, List<String> toStrList);

    UserVo getById(String id);

    Page<UserVo> page(Page<TbCoreUser> page, Wrapper<TbCoreUser> queryWrapper);
}
