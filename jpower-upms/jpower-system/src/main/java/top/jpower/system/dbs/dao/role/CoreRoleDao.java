package top.jpower.system.dbs.dao.role;


import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.util.LambdaUtil;
import org.springframework.stereotype.Repository;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleMapper;
import top.jpower.system.dbs.entity.role.CoreRole;

import java.util.List;
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
	public List<Tree<Long>> listTree() {
		return super.tree(Wrappers.getTreeWrapper(CoreRole::getId, CoreRole::getParentId)
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
}
