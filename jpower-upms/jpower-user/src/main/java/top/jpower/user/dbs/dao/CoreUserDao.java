package top.jpower.user.dbs.dao;


import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.update.UpdateWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import org.springframework.stereotype.Repository;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.api.cache.SystemCache;
import top.jpower.user.dbs.dao.mapper.CoreUserMapper;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.dbs.entity.CoreUserRole;
import top.jpower.user.vo.LoginUserVO;
import top.jpower.user.vo.UserVO;

import java.util.*;

import static com.mybatisflex.core.query.QueryMethods.groupConcat;
import static top.jpower.user.dbs.entity.table.CorePostTableDef.CORE_POST;
import static top.jpower.user.dbs.entity.table.CoreUserRoleTableDef.CORE_USER_ROLE;
import static top.jpower.user.dbs.entity.table.CoreUserTableDef.CORE_USER;

/**
 * @author mr.g
 */
@Repository
public class CoreUserDao extends JpowerServiceImpl<CoreUserMapper, CoreUser> implements BaseDaoWrapper<CoreUser> {

    public void build(UserVO userVo) {
		if (Fc.notNull(userVo.getOrgId())) {
        	userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
		}
		if (Fc.isEmpty(userVo.getRoleIds())) {
        	userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toLongList(userVo.getRoleIds()))," | "));
		}
    }

    public Pg<UserVO> pageVO(Map<String, Object> map) {
        Pg<UserVO> pg = getMapper().pageAs(PaginationContext.page(),
				Wrappers.getQueryWrapper(map, "t")
						.from(CoreUser.class).as("t")
                         .select(CORE_USER.DEFAULT_COLUMNS)
                        .select(groupConcat(CORE_USER_ROLE.ROLE_ID).as(UserVO::getRoleIds))
                        .select(CORE_POST.NAME.as(UserVO::getPostName))
                        .leftJoin(CoreUserRole.class).on(CoreUserRole::getUserId, CoreUser::getId)
                        .leftJoin(CorePost.class).on(CoreUser::getPostId, CorePost::getId)
                        .groupBy(CoreUser::getId), UserVO.class);
        return pageConvert(pg, this::build);
    }

    public List<UserVO> listVO(Map<String, Object> map) {
        List<UserVO> list = super.listAs(Wrappers.getQueryWrapper(map, "t")
						.from(CoreUser.class).as("t")
						.select(CORE_USER.DEFAULT_COLUMNS)
						.select(groupConcat(CORE_USER_ROLE.ROLE_ID).as(UserVO::getRoleIds))
						.select(CORE_POST.NAME.as(UserVO::getPostName))
						.leftJoin(CoreUserRole.class).on(CoreUserRole::getUserId, CoreUser::getId)
						.leftJoin(CorePost.class).on(CoreUser::getPostId, CorePost::getId)
						.groupBy(CoreUser::getId), UserVO.class);
        return listConvert(list, this::build);
    }

    public UserVO selectAllById(Long id) {
        UserVO userVO = super.getOneAs(Wrappers.getQueryWrapper()
                .select(CORE_USER.DEFAULT_COLUMNS)
                .select(groupConcat(CORE_USER_ROLE.ROLE_ID).as(UserVO::getRoleIds))
                .select(CORE_POST.NAME.as(UserVO::getPostName))
                .leftJoin(CoreUserRole.class).on(CoreUserRole::getUserId, CoreUser::getId)
                .leftJoin(CorePost.class).on(CoreUser::getPostId, CorePost::getId)
                .eq(CoreUser::getId, id)
                .groupBy(CoreUser::getId), UserVO.class);
        return convert(userVO, this::build);
    }

    private List<Long> getChildOrg(Long orgId){
        List<Long> listOrgId = Fc.notNull(orgId)?SystemCache.getChildIdOrgById(orgId):null;
        listOrgId = Fc.isNull(listOrgId)?new ArrayList<>():listOrgId;
        if(Fc.notNull(orgId)){
            listOrgId.add(orgId);
        }
        return listOrgId;
    }

    /**
     * 获取用户密码
     * @author mr.g
     * @param account 账号
     * @return 密码
     **/
    public String getPassword(String account) {
        return super.getObjAs(Wrappers.getQueryWrapper().select(CoreUser::getPassword).eq(CoreUser::getLoginId, account), String.class);
    }

    /**
     * 修改用户手机号
     *
     * @author mr.g
     * @param userId 用户ID
     * @param phone 新手机号
     * @return 是否成功
     **/
    public boolean updatePhone(Long userId, String phone) {
        return super.update(UpdateEntity.of(CoreUser.class).setTelephone(phone),
                Wrappers.getQueryWrapper().eq(CoreUser::getId, userId));
    }

    /**
     * 修改用户邮箱
     *
     * @author mr.g
     * @param email 邮箱
     * @param userId 用户ID
     * @return 是否成功
     **/
    public boolean updateEmail(Long userId, String email) {
        return super.updateChain().set(CoreUser::getEmail, email).eq(CoreUser::getId, userId).update();
    }

    public long countByTenant(String tenantCode) {
        return super.count(QueryCondition.create(CORE_USER.TENANT_CODE, tenantCode));
    }

    /**
     * 修改当前用户信息
     *
     * @author mr.g
     * @param userVO 用户信息
     * @return 是否成功
     **/
    public boolean updateUserInfo(LoginUserVO userVO) {
        return super.update(UpdateWrapper.of(CoreUser.class)
                        .set(CoreUser::getAvatar,userVO.getAvatar())
                        .set(CoreUser::getNickName,userVO.getRealName())
                        .set(CoreUser::getUserName,userVO.getUsername())
                        .set(CoreUser::getIdType,userVO.getIdType())
                        .set(CoreUser::getIdNo,userVO.getIdNo())
                        .set(CoreUser::getBirthday,userVO.getBirthday())
                        .set(CoreUser::getPostCode,userVO.getPostCode())
                        .set(CoreUser::getAddress,userVO.getAddress()).toEntity(),
                Wrappers.getQueryWrapper().eq(CoreUser::getId, ShieldUtil.getUserId()));
    }

    /**
     * 修改用户密码
     *
     * @author mr.g
     * @param pass 新密码
     * @param ids 用户ID
     * @return 是否成功
     **/
    public boolean updatePassword(String pass, List<Long> ids) {
        return super.update(UpdateEntity.of(CoreUser.class).setPassword(pass),
                Wrappers.getQueryWrapper().in(CoreUser::getId, ids));
    }

    public boolean updateLoginCount(Long id) {
        return super.update(UpdateWrapper.of(CoreUser.class)
                        .set(CoreUser::getLoginCount, CORE_USER.LOGIN_COUNT.add(1))
                        .set(CoreUser::getLastLoginTime, new Date()).toEntity(),
                Wrappers.getQueryWrapper().eq(CoreUser::getId,id));
    }

    /**
     * 根据角色ID分页查询用户
     *
     * @author mr.g
     * @param map 查询参数
     * @return 用户列表
     **/
    public Pg<UserVO> pageByRoleId(Map<String, Object> map) {
        return getMapper().pageAs(PaginationContext.page(), Wrappers.getQueryWrapper(map)
                        .from(CoreUser.class)
                        .leftJoin(CoreUserRole.class).on(CoreUserRole::getUserId, CoreUser::getId)
                        .orderBy(CoreUser::getCreateTime).desc()
                , UserVO.class);
    }

	/**
	 * 根据用户ID获取用户信息
	 *
	 * @author mr.g
	 * @param id 用户ID
	 * @return 用户信息
	 **/
	public LoginUserVO userInfo(Long id) {
		return super.getOneAs(Wrappers.getQueryWrapper()
				.select(CORE_USER.ID.as(LoginUserVO::getUserId))
				.select(CORE_USER.AVATAR.as(LoginUserVO::getAvatar))
				.select(CORE_USER.NICK_NAME.as(LoginUserVO::getRealName))
				.select(CORE_USER.USER_NAME.as(LoginUserVO::getUsername))
				.select(CORE_USER.ID_NO.as(LoginUserVO::getIdNo))
				.select(CORE_USER.POST_CODE.as(LoginUserVO::getPostCode))
				.select(CORE_USER.ADDRESS.as(LoginUserVO::getAddress))
				.select(CORE_USER.ID_TYPE.as(LoginUserVO::getIdType))
				.select(CORE_USER.BIRTHDAY.as(LoginUserVO::getBirthday))
				.eq(CoreUser::getId, id), LoginUserVO.class);
	}
}
