package top.jpower.user.dbs.dao;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.user.api.cache.UserCache;
import top.jpower.user.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.jpower.vo.UserVo;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.dbs.entity.CoreUserRole;
import top.jpower.user.vo.UserVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mybatisflex.core.query.QueryMethods.groupConcat;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserDao extends JpowerServiceImpl<TbCoreUserMapper, CoreUser> implements BaseDaoWrapper<CoreUser, UserVO> {

    @Override
    public UserVO build(UserVO userVo) {
        userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
        userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toLongList(userVo.getRoleIds()))," | "));
        if (Fc.notNull(userVo.getPostId())){
            userVo.setPostName(UserCache.getPostName(userVo.getPostId()));
        }
        return userVo;
    }

    public Pg<UserVO> listVo(CoreUser coreUser) {
        Pg<UserVO> pg = getMapper().pageAs(PaginationContext.page(), Wrappers.getQueryWrapper(coreUser)
                        .select(CORE_USER.DEFAULT_COLUMNS)
                        .select(groupConcat(CORE_USER_ROLE.ROLE_ID).as(UserVO::getRoleIds))
                        .leftJoin(CoreUserRole.class)
                        .on(CoreUserRole::getUserId, CoreUser::getId)
                        .groupBy(CoreUser::getId), UserVO.class);
        pageBuild(pg);
        return pg;
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
     * @param account
     * @param tenantCode
     * @return
     **/
    public String getPassword(String account, String tenantCode) {
        return super.getObj(Condition.<TbCoreUser>getQueryWrapper().lambda()
                .select(TbCoreUser::getPassword)
                .eq(TbCoreUser::getLoginId, account)
                .eq(ShieldUtil.isRoot(), TbCoreUser::getTenantCode, Fc.isBlank(tenantCode)?DEFAULT_TENANT_CODE:tenantCode), Fc::toStr);
    }

    /**
     * 修改用户手机号
     * @author mr.g
     * @param userId
     * @param phone
     * @return
     **/
    public boolean updatePhone(Long userId, String phone) {
        return super.update(Wrappers.<TbCoreUser>lambdaUpdate()
                .set(TbCoreUser::getTelephone, phone)
                .eq(TbCoreUser::getId, userId));
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
        return super.update(Wrappers.<TbCoreUser>lambdaUpdate()
                .set(TbCoreUser::getEmail, email)
                .eq(TbCoreUser::getId, userId));
    }
}
