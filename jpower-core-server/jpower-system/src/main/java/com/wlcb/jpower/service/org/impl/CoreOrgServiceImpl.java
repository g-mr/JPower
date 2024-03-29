package com.wlcb.jpower.service.org.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.org.TbCoreOrgDao;
import com.wlcb.jpower.dbs.dao.org.mapper.TbCoreOrgMapper;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.org.CoreOrgService;
import com.wlcb.jpower.vo.OrgVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("coreOrgService")
@Slf4j
public class CoreOrgServiceImpl extends BaseServiceImpl<TbCoreOrgMapper, TbCoreOrg> implements CoreOrgService {

    private TbCoreOrgDao coreOrgDao;

    @Override
    public List<OrgVo> listLazyByParent(TbCoreOrg coreOrg) {
        if (Fc.isNotBlank(coreOrg.getParentId())){
            return coreOrgDao.getBaseMapper().listLazyByParent(coreOrg);
        }
        // TODO: 2022-08-07 这块写的真恶心，有啥办法可以直接在SQL里查出最顶级，可以提供下办法 <br/>
        //  只查最顶级数据
        List<OrgVo> orgVoList = coreOrgDao.getBaseMapper().listLazyByParent(coreOrg);
        orgVoList.removeIf(o-> orgVoList.stream().anyMatch(l->Fc.equalsValue(l.getId(),o.getParentId())));
        return orgVoList;
    }

    @Override
    public Boolean add(TbCoreOrg coreOrg) {

        LambdaQueryWrapper<TbCoreOrg> queryWrapper = Condition.<TbCoreOrg>getQueryWrapper().lambda().eq(TbCoreOrg::getCode,coreOrg.getCode());
        if (ShieldUtil.isRoot() && EnvBeanUtil.getTenantEnable()){
            queryWrapper.eq(TbCoreOrg::getTenantCode,Fc.isNotBlank(coreOrg.getTenantCode())?coreOrg.getTenantCode():DEFAULT_TENANT_CODE);
        }
        JpowerAssert.geZero(coreOrgDao.count(queryWrapper), JpowerError.Business,"该编码已存在");

        if (StringUtils.isBlank(coreOrg.getParentId())){
            coreOrg.setParentId(JpowerConstants.TOP_CODE);
            coreOrg.setAncestorId(JpowerConstants.TOP_CODE);
        }else {
            coreOrg.setAncestorId(coreOrg.getParentId().concat(StringPool.COMMA).concat(Fc.toStr(coreOrgDao.getById(coreOrg.getParentId()).getAncestorId())));
        }
        return coreOrgDao.save(coreOrg);
    }

    @Override
    public long listOrgByPids(String ids) {
        return coreOrgDao.count(new QueryWrapper<TbCoreOrg>()
                .lambda().in(TbCoreOrg::getParentId, Fc.toStrList(ids)).notIn(TbCoreOrg::getId,Fc.toStrList(ids)));
    }

    @Override
    public Boolean update(TbCoreOrg coreOrg) {
        TbCoreOrg org = coreOrgDao.getById(coreOrg.getId());

        if (StringUtils.isNotBlank(coreOrg.getCode())){
            LambdaQueryWrapper<TbCoreOrg> queryWrapper = Condition.<TbCoreOrg>getQueryWrapper().lambda().eq(TbCoreOrg::getCode,coreOrg.getCode());
            if (ShieldUtil.isRoot()){
                queryWrapper.eq(TbCoreOrg::getTenantCode,org.getTenantCode());
            }
            TbCoreOrg tbCoreOrg = coreOrgDao.getOne(queryWrapper);
            if (tbCoreOrg != null && !StringUtils.equals(tbCoreOrg.getId(),coreOrg.getId())){
                throw new BusinessException("该编码已存在");
            }
        }

        if (StringUtils.isNotBlank(coreOrg.getParentId())){
            coreOrg.setAncestorId(StringUtil.replace(org.getAncestorId(),org.getParentId(),coreOrg.getParentId()));
        }

        return coreOrgDao.updateById(coreOrg);
    }

    @Override
    public List<Tree<String>> tree(Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getLambdaTreeWrapper(TbCoreOrg.class,TbCoreOrg::getId,TbCoreOrg::getParentId)
                .select(TbCoreOrg::getName)
                .orderByAsc(TbCoreOrg::getSort)
                .unLambda()
                .map(coreOrg));
    }

    @Override
    public List<Tree<String>> tree(String parentId, Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getLambdaTreeWrapper(TbCoreOrg.class,TbCoreOrg::getId,TbCoreOrg::getParentId)
                .lazy(parentId)
                .select(TbCoreOrg::getName,TbCoreOrg::getCode)
                .orderByAsc(TbCoreOrg::getSort).unLambda().map(coreOrg));
    }

    @Override
    public List<String> queryChildById(String id) {
        return coreOrgDao.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda()
                .select(TbCoreOrg::getId)
                .like(TbCoreOrg::getAncestorId,id),Fc::toStr);
    }

}
