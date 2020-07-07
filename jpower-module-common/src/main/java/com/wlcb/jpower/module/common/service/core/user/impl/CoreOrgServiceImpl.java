package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreOrgService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreOrgDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreOrgMapper;
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
    @Autowired
    private TbCoreOrgDao coreOrgDao;

    @Override
    public List<TbCoreOrg> listByParent(TbCoreOrg coreOrg) {

        QueryWrapper wrapper = new QueryWrapper<TbCoreOrg>();

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

        wrapper.orderByAsc("sort");

        return coreOrgMapper.selectList(wrapper);
    }

    @Override
    public TbCoreOrg selectOrgByCode(String code) {
        return coreOrgDao.getOne(new QueryWrapper<TbCoreOrg>().lambda().eq(TbCoreOrg::getCode,code));
    }

    @Override
    public Integer add(TbCoreOrg coreOrg) {
        coreOrg.setUpdateUser(coreOrg.getCreateUser());
        return coreOrgDao.save(coreOrg)?1:0;
    }

    @Override
    public Integer listOrgByPids(String ids) {
        return coreOrgDao.count(new QueryWrapper<TbCoreOrg>().lambda().in(TbCoreOrg::getId,ids));
    }

    @Override
    public Integer delete(String ids) {
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        return coreOrgDao.removeByIds(list)?1:0;
    }

    @Override
    public Integer update(TbCoreOrg coreUser) {
        return coreOrgDao.updateById(coreUser)?1:0;
    }

}
