package com.qidiangk.smart.system.service.role.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.constants.CacheNames;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.dbs.dao.role.CoreFunctionDao;
import com.qidiangk.smart.system.dbs.dao.role.CoreRoleDao;
import com.qidiangk.smart.system.dbs.dao.role.CoreRoleFunctionDao;
import com.qidiangk.smart.system.dbs.dao.role.CoreRoleMenuDao;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleMapper;
import com.qidiangk.smart.system.dbs.entity.role.CoreRole;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleMenu;
import com.qidiangk.smart.system.service.role.CoreRoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.qidiangk.smart.common.constants.ServiceCodeConstants.DELETE_EXIST_CHILD;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE_LONG;

/**
 * 角色服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreRoleServiceImpl extends BaseServiceImpl<CoreRoleMapper, CoreRole> implements CoreRoleService {

    private final CoreRoleDao coreRoleDao;
    private final CoreRoleFunctionDao coreRoleFunctionDao;
    private final CoreFunctionDao coreFunctionDao;
    private final CoreRoleMenuDao coreRoleMenuDao;

	/**
	 * 构建角色的z祖级ID
	 *
	 * @param coreRole 角色
	 */
	private void buildAncestorId(CoreRole coreRole) {
		Optional<String> ancestorIdOpt = coreRoleDao.getAncestorIdByIdOpt(coreRole.getParentId());
		String ancestorId = ancestorIdOpt.orElse(TOP_CODE);
		if (ancestorIdOpt.isPresent()) {
			ancestorId = ancestorId + StringPool.COMMA + coreRole.getParentId();
		}
		coreRole.setAncestorId(ancestorId);
	}

    @Override
    public Boolean add(CoreRole coreRole) {
		coreRole.setParentId(Fc.toLong(coreRole.getParentId(), TOP_CODE_LONG));
		coreRole.setIsSysRole(Fc.toBoolean(coreRole.getIsSysRole(), Boolean.FALSE));

		buildAncestorId(coreRole);
        if (coreRoleDao.save(coreRole)) {
			CacheUtil.clear(CacheNames.ROLE_KEY);
            List<Long> functionIds = coreFunctionDao.queryNoMenuIdByTop();
            return coreRoleFunctionDao.saveFunctions(functionIds, coreRole.getId());
        }
        return false;
    }

	@Override
	public boolean removeByIds(List<Long> ids) {
		JpowerAssert.notTrue(coreRoleDao.existsInField(CoreRole::getParentId, ids), JpowerError.Business, DELETE_EXIST_CHILD);

		CacheUtil.clear(CacheNames.ROLE_KEY);
		CacheUtil.clear(CacheNames.FUNCTION_KEY);
		CacheUtil.clear(CacheNames.DATASCOPE_KEY);
		CacheUtil.clear(CacheNames.USER_KEY);
		return coreRoleDao.remove(Wrappers.getQueryWrapper()
				.in(CoreRole::getId, ids)
				.eq(CoreRole::getIsSysRole, YN01Enum.N.getValue()));
	}

	@Override
	public boolean updateById(CoreRole coreRole) {
		buildAncestorId(coreRole);

		CacheUtil.clear(CacheNames.ROLE_KEY);
		return coreRoleDao.updateById(coreRole);
	}

    @Override
    public boolean saveTopMenu(Long roleId, List<Long> menuIds) {

        coreRoleMenuDao.removeRealByRoleId(roleId);

        if (Fc.isEmpty(menuIds)){
            return true;
        }

        List<CoreRoleMenu> roleMenuList = new ArrayList<>();
        menuIds.forEach(id->{
			CoreRoleMenu roleMenu = new CoreRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(id);
            roleMenuList.add(roleMenu);
        });

        return coreRoleMenuDao.saveBatch(roleMenuList);
    }

	@Override
	public List<Tree<Long>> listTree(Map<String, Object> params) {
		return coreRoleDao.listTree(params);
	}

	@Override
	public List<Tree<Long>> treeSelect() {
		return coreRoleDao.treeSelect();
	}

	@Override
	public List<Long> queryMenuIdByRoleId(Long roleId) {
		return coreRoleMenuDao.queryMenuIdByRoleId(roleId);
	}

	@Override
	public List<String> getRoleNameByIds(List<Long> roleIds) {
		if (Fc.isEmpty(roleIds)) {
			return ListUtil.empty();
		}
		return coreRoleDao.getRoleNameByIds(roleIds);
	}
}
