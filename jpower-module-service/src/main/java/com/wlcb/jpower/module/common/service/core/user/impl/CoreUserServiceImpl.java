package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreOrgDao;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreUserMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.gmac
 */
@Slf4j
@AllArgsConstructor
@Service("coreUserService")
public class CoreUserServiceImpl extends BaseServiceImpl<TbCoreUserMapper,TbCoreUser> implements CoreUserService {

    private TbCoreUserDao coreUserDao;
    private TbCoreOrgDao coreOrgDao;
    private TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public PageInfo<TbCoreUser> listPage(TbCoreUser coreUser,String orgCode) {
        List<String> listOrgId = coreOrgDao.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda()
                .select(TbCoreOrg::getId)
                .likeRight(TbCoreOrg::getCode,orgCode),Fc::toStr);

        PaginationContext.startPage();
        List<TbCoreUser> list = coreUserDao.getBaseMapper().selectUserList(coreUser,listOrgId);
        return new PageInfo<>(list);
    }

    @Override
    public List<TbCoreUser> list(TbCoreUser coreUser,String orgCode) {
        List<String> listOrgId = coreOrgDao.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda()
                .select(TbCoreOrg::getId)
                .likeRight(TbCoreOrg::getCode,orgCode),Fc::toStr);

        List<TbCoreUser> list = coreUserDao.getBaseMapper().selectUserList(coreUser,listOrgId);
        return list;
    }

    @Override
    public boolean save(TbCoreUser coreUser) {
        if (Fc.isNull(coreUser.getActivationStatus())){
            Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION, ConstantsUtils.DEFAULT_USER_ACTIVATION);
            coreUser.setActivationStatus(isActivation);
        }

        if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UUIDUtil.create10UUidNum());
            coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
        }

        return coreUserDao.save(coreUser);
    }

    @Override
    public Boolean delete(String ids) {
        List<String> list = Fc.toStrList(ids);
        boolean is = coreUserDao.removeByIds(list);
        if (is){
            coreUserRoleDao.removeReal(new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,list));
        }
        return is;
    }

    @Override
    public Boolean update(TbCoreUser coreUser) {
        boolean is = coreUserDao.updateById(coreUser);
        //如果成功并且存在角色则去修改角色
        if (is && Fc.isNotBlank(coreUser.getRoleIds())){
            updateUsersRole(coreUser.getId(),coreUser.getRoleIds());
        }
        return is;
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId) {
        return coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getLoginId,loginId));
    }

    @Override
    public TbCoreUser selectUserById(String id) {
        return baseMapper.selectAllById(id);
    }

    @Override
    public Boolean updateUserPassword(String ids, String pass) {
        UpdateWrapper wrapper = new UpdateWrapper<TbCoreUser>();
        wrapper.in("id",ids);
        wrapper.set("password",pass);
        return coreUserDao.update(new TbCoreUser(),wrapper);
    }

    @Override
    public Boolean insterBatch(List<TbCoreUser> list) {
        return coreUserDao.saveBatch(list);
    }

    @Override
    public Boolean updateUsersRole(String userIds, String roleIds) {
        //先删除用户原有角色
        String[] uIds = userIds.split(StringPool.COMMA);

        QueryWrapper wrapper = new QueryWrapper<TbCoreUserRole>();
        wrapper.in("user_id",uIds);
        coreUserRoleDao.removeReal(wrapper);

        if (Fc.isNotBlank(roleIds)){
            String[] rIds = roleIds.split(StringPool.COMMA);
            List<TbCoreUserRole> userRoles = new ArrayList<>();
            for (String rId : rIds) {
                for (String userId : uIds) {
                    TbCoreUserRole userRole = new TbCoreUserRole();
                    userRole.setId(UUIDUtil.getUUID());
                    userRole.setUserId(userId);
                    userRole.setRoleId(rId);
                    userRoles.add(userRole);
                }
            }

            if (userRoles.size() > 0){
                Boolean is = coreUserRoleDao.saveBatch(userRoles);
                return is;
            }
        }
        return true;
    }

    @Override
    public TbCoreUser selectByPhone(String phone) {
        QueryWrapper wrapper = new QueryWrapper<TbCoreUserRole>();
        wrapper.eq("telephone",phone);
        return coreUserDao.getOne(wrapper);
    }

    @Override
    public TbCoreUser selectByUserNameAndId(String id, String username) {
        LambdaQueryWrapper<TbCoreUser> wrapper = new QueryWrapper<TbCoreUser>().lambda();
        wrapper.eq(TbCoreUser::getLoginId,username);
        wrapper.eq(TbCoreUser::getId,id);
        return coreUserDao.getOne(wrapper);
    }

    @Override
    public Boolean updateLoginInfo(TbCoreUser user) {
        LambdaUpdateWrapper<TbCoreUser> wrapper = new UpdateWrapper<TbCoreUser>().lambda()
        .set(TbCoreUser::getLoginCount,user.getLoginCount())
        .set(TbCoreUser::getLastLoginTime,user.getLastLoginTime())
        .eq(TbCoreUser::getId,user.getId());
        return coreUserDao.update(new TbCoreUser(),wrapper);
    }

}
