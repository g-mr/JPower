package com.wlcb.jpower.service.org.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.org.TbCoreOrgDao;
import com.wlcb.jpower.dbs.dao.org.mapper.TbCoreOrgMapper;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.vo.OrgVo;
import com.wlcb.jpower.wrapper.BaseDictWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("coreOrgService")
public class CoreOrgServiceImpl extends BaseServiceImpl<TbCoreOrgMapper, TbCoreOrg> implements CoreOrgService {

    private final String sql = "(select code from tb_core_org where id in ({}))";

    @Autowired
    private TbCoreOrgDao coreOrgDao;

    @Override
    public List<OrgVo> listLazyByParent(TbCoreOrg coreOrg) {
        return BaseDictWrapper.dict(coreOrgDao.getBaseMapper().listLazyByParent(coreOrg),OrgVo.class);
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
        return coreOrgDao.count(new QueryWrapper<TbCoreOrg>()
                .lambda().in(TbCoreOrg::getParentId, Fc.toStrList(ids)).notIn(TbCoreOrg::getId,Fc.toStrList(ids)));
    }

    @Override
    public Boolean update(TbCoreOrg coreUser) {
        return coreOrgDao.updateById(coreUser);
    }

    @Override
    public List<Node> tree(Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getTreeWrapper(TbCoreOrg::getId,TbCoreOrg::getParentId,TbCoreOrg::getName).map(coreOrg).lambda().orderByAsc(TbCoreOrg::getSort));
    }

    @Override
    public List<Node> tree(String parentId, Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getTreeWrapper(TbCoreOrg::getId,TbCoreOrg::getParentId,TbCoreOrg::getName,TbCoreOrg::getCode)
                .lazy(parentId).map(coreOrg)
                .lambda()
                .orderByAsc(TbCoreOrg::getSort));
    }

    @Override
    public List<String> queryChildById(String id) {
        return coreOrgDao.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda()
                .select(TbCoreOrg::getId)
                .like(TbCoreOrg::getAncestorId,id),Fc::toStr);
    }

}
