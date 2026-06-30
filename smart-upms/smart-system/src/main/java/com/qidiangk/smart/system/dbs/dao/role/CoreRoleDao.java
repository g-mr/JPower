package com.qidiangk.smart.system.dbs.dao.role;


import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.util.LambdaUtil;
import org.springframework.stereotype.Repository;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleMapper;
import com.qidiangk.smart.system.dbs.entity.role.CoreRole;
import com.qidiangk.smart.system.dbs.entity.tenant.CoreTenant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 角色数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleDao extends JpowerServiceImpl<CoreRoleMapper, CoreRole> {

	/**
	 * 获取角色树
	 *
	 * @return 角色树
	 */
	public List<Tree<Long>> listTree(Map<String, Object> params) {
		return super.tree(Wrappers.getTreeWrapper(CoreRole::getId, CoreRole::getParentId)
						.map(params, "t")
						.from(CoreRole.class).as("t")
						.select(CoreRole::getAlias, CoreRole::getName, CoreRole::getIsSysRole, CoreRole::getRemark, CoreRole::getSort, CoreRole::getTenantCode)
						.select(CoreTenant::getTenantName)
						.leftJoin(CoreTenant.class).on(CoreRole::getTenantCode, CoreTenant::getTenantCode)
						.orderBy(CoreRole::getCreateTime).desc());
	}

	/**
	 * 获取角色树
	 *
	 * @return 角色树
	 */
	public List<Tree<Long>> treeSelect() {
		return super.tree(Wrappers.getTreeWrapper(CoreRole::getId, CoreRole::getParentId)
				.select(CoreRole::getAlias, CoreRole::getName)
				.where(q->{
					if (!ShieldUtil.isRoot()){
						List<Long> roleId = ShieldUtil.getUserRole();
						q.in(CoreRole::getId, roleId).or(LambdaUtil.getFieldName(CoreRole::getAncestorId)+" regexp ?", Fc.join(roleId, StringPool.SPILT));
					}
				})
				.orderBy(CoreRole::getCreateTime).desc());
	}

	/**
	 * 获取角色的祖级ID
	 *
	 * @param parentId 父级ID
	 * @return 角色的父级ID
	 */
	public Optional<String> getAncestorIdByIdOpt(Long parentId) {
		return super.getObjAsOpt(Wrappers.getQueryWrapper().select(CoreRole::getAncestorId).eq(CoreRole::getId, parentId), String.class);
	}

	/**
	 * 获取角色名称
	 *
	 * @param roleIds 角色ID
	 * @return 角色名称
	 */
	public List<String> getRoleNameByIds(List<Long> roleIds) {
		return super.objListAs(Wrappers.getQueryWrapper().select(CoreRole::getName).in(CoreRole::getId,roleIds), String.class);
	}
}
