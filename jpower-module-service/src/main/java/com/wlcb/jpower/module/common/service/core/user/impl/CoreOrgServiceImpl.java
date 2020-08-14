package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreOrgService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreOrgDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreOrgMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import com.wlcb.jpower.module.mp.support.Condition;
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
        return coreOrgMapper.selectList(Condition.getQueryWrapper(coreOrg).lambda().orderByAsc(TbCoreOrg::getSort));
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
                .lambda().in(TbCoreOrg::getParentCode, Fc.toStrList(ids)));
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
