package top.jpower.user.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.pojo.LoginUserVO;
import top.jpower.user.pojo.UserByRoleBO;
import top.jpower.user.pojo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreUserService extends BaseService<CoreUser> {

    /**
     * 查询用户列表
     *
     * @author mr.g
     **/
    Pg<UserVO> listPage(Map<String, Object> map);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增登录用户
     * @Date 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    @Override
    boolean save(CoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量删除用户
     * @Date 11:28 2020-05-19
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Boolean deleteByIds(List<Long> ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 修改用户
     * @Date 11:36 2020-05-19
     * @Param [coreUser]
     * @return java.lang.Integer
     **/
    Boolean updateUser(CoreUser coreUser);

    /**
     * @Author 郭丁志
     * @Description //TODO 该用户名已存在
     * @Date 17:20 2020-05-19
     * @Param [loginId]
     * @return top.jpower.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    CoreUser selectUserLoginId(String loginId,String tenantCode);

    /**
     * @author 郭丁志
     * @Description //TODO 通过id查询用户信息
     * @date 1:27 2020/5/24 0024
     * @param id 用户id
     * @return top.jpower.jpower.module.dbs.entity.core.user.TbCoreUser
     */
    UserVO selectUserById(Long id);

    /**
     * @author 郭丁志
     * @Description //TODO 修改用户密码
     * @date 1:28 2020/5/24 0024
     * @param ids 用户id
     * @return java.lang.Integer
     */
    boolean resetPassword(List<Long> ids);

    /**
     * @author 郭丁志
     * @Description //TODO 批量新增
     * @date 2:55 2020/5/24 0024
     * @param list
     * @return java.lang.Integer
     */
    boolean insertBatch(List<CoreUser> list,boolean isCover);

    /**
     * 更新用户角色
     *
     * @author mr.g
     * @param userIds 用户ID 多个逗号分隔
     * @param roleIds  角色ID 多个逗号分隔
     * @return 是否成功
     */
    Boolean updateUsersRole(List<Long> userIds, List<Long> roleIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过手机号查找用户
     * @Date 10:38 2020-07-03
     * @Param [phone]
     * @return top.jpower.jpower.module.dbs.entity.core.user.TbCoreUser
     **/
    CoreUser selectByPhone(String phone,String tenantCode);

    /**
     * @author 郭丁志
     * @Description //TODO 更新用户登陆信息
     * @date 0:02 2020/10/21 0021
     * @param id
     */
    Boolean updateLoginCount(Long id);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询用户列表
     * @Date 14:49 2020-08-20
     * @Param [coreUser, orgCode]
     **/
    List<UserVO> list(Map<String, Object> map);

    /**
     * @author 郭丁志
     * @Description //TODO 通过第三方验证码查询用户
     * @date 0:08 2020/10/21 0021
     * @param otherCode
     * @param tenantCode
     */
    CoreUser selectUserByOtherCode(String otherCode, String tenantCode);

    /**
     * @author 郭丁志
     * @Description // 创建管理员用户
     * @date 0:09 2020/10/25 0025
     * @param user 用户信息
     */
    Long saveUser(CoreUser user, List<Long> roleIds);

    /**
     * 为角色添加用户
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param userIds 用户ID
     * @return 是否成功
     **/
    boolean addRoleUsers(Long roleId, List<Long> userIds);

    /**
     * 为角色删除用户
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param userIds 用户ID
     * @return 是否成功
     **/
    boolean deleteRoleUsers(Long roleId, List<Long> userIds);

    /**
     * 验证登录名的密码
     *
     * @author mr.g
     * @param account 登录名
     * @param password 密码
     * @param tenantCode 租户
     * @return 密码是否正确
     **/
    boolean validatePassword(String account, String password, String tenantCode);

    /**
     * 修改用户手机号
     *
     * @author mr.g
     * @param userId 用户ID
     * @param phone 要修改的手机号
     * @return 是否成功
     **/
    boolean updatePhone(String phone, Long userId);

    /**
     * 修改用户邮箱
     *
     * @author mr.g
     * @param email 邮箱
     * @param userId 用户ID
     * @return 是否成功
     **/
    boolean updateEmail(String email, Long userId);

    boolean createUser(CoreUser coreUser);

    boolean updateUserInfo(LoginUserVO userVO);

    boolean updatePassword(String oldPw, String newPw);

    /**
     * 通过角色ID查询用户信息
     *
     * @author mr.g
     * @param map 查询条件
     * @return 分页用户列表
     **/
    Pg<UserVO> pageByRoleId(UserByRoleBO query);

	/**
	 *据用户id查询用户信息
	 * @param id 用户id
	 * @return 用户信息
	 */
	LoginUserVO userInfo(Long id);

	/**
	 * 启用禁用用户
	 * @param id 用户id
	 * @param status 是否启用
	 * @return 是否成功
	 */
	boolean enable(Long id, Boolean status);

	/**
	 * 根据租户代码删除用户
	 * @param tenantCodes 租户代码
	 * @return 是否成功
	 */
	boolean removeTenantAll(List<String> tenantCodes);

}
