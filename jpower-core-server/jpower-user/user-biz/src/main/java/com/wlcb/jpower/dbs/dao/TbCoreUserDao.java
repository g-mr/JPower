package com.wlcb.jpower.dbs.dao;


import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.BaseDaoWrapper;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.vo.UserVo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserDao extends JpowerServiceImpl<TbCoreUserMapper, TbCoreUser> implements BaseDaoWrapper<TbCoreUser,UserVo> {

    @Override
    public UserVo conver(TbCoreUser user) {
        UserVo userVo = Objects.requireNonNull(BeanUtil.copy(user, UserVo.class));
        userVo.setOrgName(SystemCache.getOrgName(userVo.getOrgId()));
        userVo.setRoleName(Fc.join(SystemCache.getRoleNameByIds(Fc.toStrList(user.getRoleIds()))," | "));
        return userVo;
    }

    public List<UserVo> listVo(TbCoreUser coreUser) {
        List<TbCoreUser> list = getBaseMapper().selectUserList(coreUser,getChildOrg(coreUser.getOrgId()));
        return listConver(list);
    }

    private List<String> getChildOrg(String orgId){
        List<String> listOrgId = Fc.isNotBlank(orgId)?SystemCache.getChildIdOrgById(orgId):null;
        listOrgId = Fc.isNull(listOrgId)?new ArrayList<>():listOrgId;
        if(Fc.isNotBlank(orgId)){
            listOrgId.add(orgId);
        }
        return listOrgId;
    }

}
