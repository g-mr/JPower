package top.jpower.system.service.org.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.org.CoreOrgDao;
import top.jpower.system.dbs.dao.org.mapper.CoreOrgMapper;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.service.org.CoreOrgService;
import top.jpower.system.vo.OrgVO;

import java.util.List;
import java.util.Map;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;

/**
 * 组织机构服务实现
 * 
 * @author mr.g
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoreOrgServiceImpl extends BaseServiceImpl<CoreOrgMapper, CoreOrg> implements CoreOrgService {

    private final CoreOrgDao coreOrgDao;

    @Override
    public List<OrgVO> listLazyByParent(Map<String, Object> map) {
		map.putIfAbsent("parentId_eq", JpowerConstants.TOP_CODE_LONG);
		return coreOrgDao.listLazy(map);
    }

	@Override
	public List<OrgVO> pageTop(Map<String, Object> map) {
		map.put("parentId_eq", JpowerConstants.TOP_CODE_LONG);
		return coreOrgDao.listLazy(map);
	}

    @Override
    public Long create(CoreOrg coreOrg) {
        JpowerAssert.notTrue(coreOrgDao.existsByField(CoreOrg::getCode,coreOrg.getCode()), JpowerError.Business, CODE_EXIST);

        if (Fc.isNull(coreOrg.getParentId()) || Fc.equalsValue(coreOrg.getParentId(), JpowerConstants.TOP_CODE_LONG)){
            coreOrg.setParentId(Fc.toLong(JpowerConstants.TOP_CODE));
            coreOrg.setAncestorId(JpowerConstants.TOP_CODE);
        } else {
            coreOrg.setAncestorId(Fc.toStr(coreOrg.getParentId()).concat(StringPool.COMMA).concat(Fc.toStr(coreOrgDao.getById(coreOrg.getParentId()).getAncestorId())));
        }
		CacheUtil.clear(CacheNames.ORG_KEY);
        coreOrgDao.save(coreOrg);
		return coreOrg.getId();
    }

    @Override
    public long countByParentids(List<Long> ids) {
        return coreOrgDao.countInField(CoreOrg::getParentId, ids);
    }

    @Override
    public Boolean update(CoreOrg coreOrg) {
        CoreOrg org = coreOrgDao.getById(coreOrg.getId());

        if (StringUtils.isNotBlank(coreOrg.getCode())){
            CoreOrg tbCoreOrg = coreOrgDao.getOneByField(CoreOrg::getCode, coreOrg.getCode());
            if (tbCoreOrg != null && !NumberUtil.equals(tbCoreOrg.getId(),coreOrg.getId())){
                JpowerAssert.createException(JpowerError.Business, CODE_EXIST);
            }
        }

        if (Fc.notNull(coreOrg.getParentId())){
            coreOrg.setAncestorId(StringUtil.replace(org.getAncestorId(),Fc.toStr(org.getParentId()),Fc.toStr(coreOrg.getParentId())));
        }

		CacheUtil.clear(CacheNames.ORG_KEY);
        return coreOrgDao.updateById(coreOrg);
    }

    @Override
    public List<Tree<Long>> tree(Map<String, Object> map) {
        return coreOrgDao.tree(map);
    }

    @Override
    public List<Tree<Long>> tree(Long parentId, Map<String, Object> map) {
        return coreOrgDao.tree(parentId, map);
    }

    @Override
    public List<Long> queryChildIdById(Long id) {
        return coreOrgDao.queryChildIdById(id);
    }

}
