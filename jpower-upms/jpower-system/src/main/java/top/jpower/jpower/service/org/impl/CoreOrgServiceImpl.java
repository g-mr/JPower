package top.jpower.jpower.service.org.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.dao.org.TbCoreOrgDao;
import top.jpower.jpower.dbs.dao.org.mapper.TbCoreOrgMapper;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.module.tenant.JpowerTenantProperties;
import top.jpower.jpower.service.org.CoreOrgService;
import top.jpower.jpower.vo.OrgVo;

import java.util.List;
import java.util.Map;

import static top.jpower.jpower.module.tenant.TenantConstant.DEFAULT_TENANT_CODE;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("coreOrgService")
@Slf4j
public class CoreOrgServiceImpl extends BaseServiceImpl<TbCoreOrgMapper, TbCoreOrg> implements CoreOrgService {

    private JpowerTenantProperties tenantProperties;
    private TbCoreOrgDao coreOrgDao;

    @Override
    public List<OrgVo> listLazyByParent(TbCoreOrg coreOrg) {
        if (Fc.notNull(coreOrg.getParentId())){
            return coreOrgDao.getBaseMapper().listLazyByParent(coreOrg);
        }
        // TODO: 2022-08-07 这块写的真恶心，有啥办法可以直接在SQL里查出最顶级，可以提供下办法 <br/>
        //  只查最顶级数据
        List<OrgVo> orgVoList = coreOrgDao.getBaseMapper().listLazyByParent(coreOrg);
        orgVoList.removeIf(o-> orgVoList.stream().anyMatch(l-> NumberUtil.equals(l.getId(),o.getParentId())));
        return orgVoList;
    }

    @Override
    public Boolean add(TbCoreOrg coreOrg) {

        LambdaQueryWrapper<TbCoreOrg> queryWrapper = Condition.<TbCoreOrg>getQueryWrapper().lambda().eq(TbCoreOrg::getCode,coreOrg.getCode());
        if (ShieldUtil.isRoot() && tenantProperties.getEnable()){
            queryWrapper.eq(TbCoreOrg::getTenantCode,Fc.isNotBlank(coreOrg.getTenantCode())?coreOrg.getTenantCode():DEFAULT_TENANT_CODE);
        }
        JpowerAssert.geZero(coreOrgDao.count(queryWrapper), JpowerError.Business,"该编码已存在");

        if (Fc.isNull(coreOrg.getParentId())){
            coreOrg.setParentId(Fc.toLong(JpowerConstants.TOP_CODE));
            coreOrg.setAncestorId(JpowerConstants.TOP_CODE);
        }else {
            coreOrg.setAncestorId(Fc.toStr(coreOrg.getParentId()).concat(StringPool.COMMA).concat(Fc.toStr(coreOrgDao.getById(coreOrg.getParentId()).getAncestorId())));
        }
        return coreOrgDao.save(coreOrg);
    }

    @Override
    public long listOrgByPids(List<Long> ids) {
        return coreOrgDao.count(new QueryWrapper<TbCoreOrg>()
                .lambda().in(TbCoreOrg::getParentId, ids).notIn(TbCoreOrg::getId,ids));
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
            if (tbCoreOrg != null && !NumberUtil.equals(tbCoreOrg.getId(),coreOrg.getId())){
                JpowerAssert.createException(JpowerError.Business,"该编码已存在");
            }
        }

        if (Fc.notNull(coreOrg.getParentId())){
            coreOrg.setAncestorId(StringUtil.replace(org.getAncestorId(),Fc.toStr(org.getParentId()),Fc.toStr(coreOrg.getParentId())));
        }

        return coreOrgDao.updateById(coreOrg);
    }

    @Override
    public List<Tree<Long>> tree(Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getLambdaTreeWrapper(TbCoreOrg.class,TbCoreOrg::getId,TbCoreOrg::getParentId)
                .select(TbCoreOrg::getName)
                .orderByAsc(TbCoreOrg::getSort)
                .unLambda()
                .map(coreOrg));
    }

    @Override
    public List<Tree<Long>> tree(Long parentId, Map<String, Object> coreOrg) {
        return coreOrgDao.tree(Condition.getLambdaTreeWrapper(TbCoreOrg.class,TbCoreOrg::getId,TbCoreOrg::getParentId)
                .lazy(parentId)
                .select(TbCoreOrg::getName,TbCoreOrg::getCode)
                .orderByAsc(TbCoreOrg::getSort).unLambda().map(coreOrg));
    }

    @Override
    public List<Long> queryChildById(Long id) {
        return coreOrgDao.listObjs(Condition.<TbCoreOrg>getQueryWrapper().lambda()
                .select(TbCoreOrg::getId)
                .like(TbCoreOrg::getAncestorId,id),Fc::toLong);
    }

}
