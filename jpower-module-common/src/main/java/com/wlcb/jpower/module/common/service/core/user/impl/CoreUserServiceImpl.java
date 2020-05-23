package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreUserService")
public class CoreUserServiceImpl implements CoreUserService {

    @Autowired
    private TbCoreUserMapper coreUserMapper;

    @Override
    public List<TbCoreUser> list(TbCoreUser coreUser) {

        EntityWrapper wrapper = new EntityWrapper<TbCoreUser>();

        if (StringUtils.isNotBlank(coreUser.getOrgId())){
            wrapper.eq("org_id",coreUser.getOrgId());
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())){
            wrapper.eq("login_id",coreUser.getLoginId());
        }

        if (StringUtils.isNotBlank(coreUser.getUserName())){
            wrapper.eq("user_name",coreUser.getUserName());
        }

        if (StringUtils.isNotBlank(coreUser.getIdNo())){
            wrapper.like("id_no",coreUser.getIdNo());
        }

        if (coreUser.getUserType() != null){
            wrapper.eq("user_type",coreUser.getUserType());
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())){
            wrapper.like("telephone",coreUser.getTelephone());
        }

        if (coreUser.getActivationStatus() != null){
            wrapper.eq("activation_status",coreUser.getActivationStatus());
        }

        if (coreUser.getStatus() != null){
            wrapper.eq("status",coreUser.getStatus());
        }

        wrapper.orderBy("create_time",false);

        return coreUserMapper.selectList(wrapper);
    }

    @Override
    public Integer add(TbCoreUser coreUser) {
        if (coreUser.getActivationStatus() == null){
            Integer isActivation = ParamConfig.getInt("isActivation");

            coreUser.setActivationStatus(isActivation);
        }

        if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UUIDUtil.create10UUidNum());
            coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
        }

        coreUser.setUpdateUser(coreUser.getCreateUser());
        return coreUserMapper.insert(coreUser);
    }

    @Override
    public Integer delete(String ids) {
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        return coreUserMapper.deleteBatchIds(list);
    }

    @Override
    public Integer update(TbCoreUser coreUser) {
        return coreUserMapper.updateById(coreUser);
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId) {
        TbCoreUser coreUser = new TbCoreUser();
        coreUser.setLoginId(loginId);
        return coreUserMapper.selectOne(coreUser);
    }
}
