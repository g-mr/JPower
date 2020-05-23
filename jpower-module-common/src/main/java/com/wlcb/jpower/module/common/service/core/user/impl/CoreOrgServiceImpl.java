package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreOrgService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreOrgMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreOrgService")
public class CoreOrgServiceImpl implements CoreOrgService {

    @Autowired
    private TbCoreOrgMapper coreOrgMapper;

    @Override
    public List<TbCoreOrg> listByParent(TbCoreOrg coreOrg) {

        EntityWrapper wrapper = new EntityWrapper<TbCoreOrg>();

        if (StringUtils.isNotBlank(coreOrg.getCode())){
            wrapper.eq("code",coreOrg.getCode());
        }

        if (StringUtils.isNotBlank(coreOrg.getName())){
            wrapper.like("name",coreOrg.getName());
        }

        if (StringUtils.isNotBlank(coreOrg.getParentId())){
            wrapper.eq("parent_id",coreOrg.getParentId());
        }else {
            wrapper.isNull("parent_id");
        }

        if (StringUtils.isNotBlank(coreOrg.getParentCode())){
            wrapper.eq("parent_code",coreOrg.getParentCode());
        }else {
            wrapper.isNull("parent_code");
        }

        if (StringUtils.isNotBlank(coreOrg.getHeadName())){
            wrapper.eq("head_name",coreOrg.getHeadName());
        }

        if (StringUtils.isNotBlank(coreOrg.getHeadPhone())){
            wrapper.eq("head_phone",coreOrg.getHeadPhone());
        }

        if (StringUtils.isNotBlank(coreOrg.getContactName())){
            wrapper.eq("contact_name",coreOrg.getContactName());
        }

        if (StringUtils.isNotBlank(coreOrg.getContactPhone())){
            wrapper.eq("contact_phone",coreOrg.getContactPhone());
        }

        if (coreOrg.getIsVirtual() != null){
            wrapper.eq("is_virtual",coreOrg.getIsVirtual());
        }

        wrapper.orderBy("sort",true);

        return coreOrgMapper.selectList(wrapper);
    }

    @Override
    public TbCoreOrg selectOrgByCode(String code) {
        TbCoreOrg org = new TbCoreOrg();
        org.setCode(code);
        return coreOrgMapper.selectOne(org);
    }

    @Override
    public Integer add(TbCoreOrg coreOrg) {
        coreOrg.setUpdateUser(coreOrg.getCreateUser());
        return coreOrgMapper.insert(coreOrg);
    }

    @Override
    public Integer listOrgByPids(String ids) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreOrg>();
        wrapper.in("id",ids);
        return coreOrgMapper.selectCount(wrapper);
    }

    @Override
    public Integer delete(String ids) {
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        return coreOrgMapper.deleteBatchIds(list);
    }

    @Override
    public Integer update(TbCoreOrg coreUser) {
        return coreOrgMapper.updateById(coreUser);
    }

}
