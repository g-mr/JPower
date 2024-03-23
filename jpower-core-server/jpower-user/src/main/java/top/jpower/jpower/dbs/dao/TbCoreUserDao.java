package top.jpower.jpower.dbs.dao;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.common.utils.BeanUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.dbs.dao.BaseDaoWrapper;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.vo.UserVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.jpower.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserDao extends JpowerServiceImpl<TbCoreUserMapper, TbCoreUser> implements BaseDaoWrapper<TbCoreUser,UserVo> {

    @Override
    public UserVo conver(TbCoreUser user) {
        if (Fc.isNull(user)){
            return null;
        }
        UserVo userVo = Objects.requireNonNull(BeanUtil.copyProperties(user, UserVo.class));
        userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
        userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toLongList(user.getRoleIds()))," | "));
        if (Fc.notNull(userVo.getPostId())){
            userVo.setPostName(UserCache.getPostName(userVo.getPostId()));
        }
        return userVo;
    }

    public List<UserVo> listVo(TbCoreUser coreUser) {
        List<TbCoreUser> list = getBaseMapper().selectUserList(coreUser,getChildOrg(coreUser.getOrgId()));
        return listConver(list);
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
