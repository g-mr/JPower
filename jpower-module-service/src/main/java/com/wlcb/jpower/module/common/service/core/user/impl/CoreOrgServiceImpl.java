package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreOrgService;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreOrgDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreOrgMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import com.wlcb.jpower.module.mp.support.Condition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("coreOrgService")
public class CoreOrgServiceImpl extends BaseServiceImpl<TbCoreOrgMapper,TbCoreOrg> implements CoreOrgService {

    private final String sql = "(select code from tb_core_org where id in ({}))";

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

        if (StringUtils.isNotBlank(coreOrg.getParentCode())){
            wrapper.eq("parent_code",coreOrg.getParentCode());
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
    public Boolean add(TbCoreOrg coreOrg) {
        return coreOrgDao.save(coreOrg);
    }

    @Override
    public Integer listOrgByPids(String ids) {
        ids = "'"+ids.replaceAll(",","','")+"'";
        return coreOrgDao.count(new QueryWrapper<TbCoreOrg>()
                .lambda().inSql(TbCoreOrg::getParentCode,StringUtil.format(sql,ids)));
    }

    @Override
    public boolean delete(String ids) {
        UpdateWrapper wrapper = new UpdateWrapper<TbCoreOrg>();
        wrapper.in("id",ids.split(","));
        wrapper.set("status",0);
        return coreOrgDao.update(new TbCoreOrg(),wrapper);
    }

    @Override
    public Boolean update(TbCoreOrg coreUser) {
        return coreOrgDao.updateById(coreUser);
    }

    @Override
    public List<Node> tree(Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getTreeWrapper(TbCoreOrg::getCode,TbCoreOrg::getParentCode,TbCoreOrg::getName).map(coreOrg).lambda().orderByAsc(TbCoreOrg::getSort));
    }

    @Override
    public List<Node> tree(String parentCode, Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getTreeWrapper(TbCoreOrg::getCode,TbCoreOrg::getParentCode,TbCoreOrg::getName).lazy(parentCode).map(coreOrg).lambda().orderByAsc(TbCoreOrg::getSort));
    }

}
