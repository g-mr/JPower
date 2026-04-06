package top.jpower.system.dbs.dao.org;


import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.system.dbs.dao.org.mapper.CoreOrgMapper;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.vo.OrgVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static top.jpower.system.dbs.entity.org.table.CoreOrgTableDef.CORE_ORG;

/**
 * 组织机构数据访问对象
 * 
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class CoreOrgDao extends JpowerServiceImpl<CoreOrgMapper, CoreOrg> {

	/**
	 * 获取列表
	 * <p>
	 * 方案：使用应用层处理 hasChildren 字段，避免 MyBatis-Flex 嵌套子查询的参数绑定问题
	 * </p>
	 *
	 * @param map
	 * @return
	 */
	public List<OrgVO> listLazy(Map<String, Object> map) {
		// 先查询主数据（不包含 hasChildren）
		List<OrgVO> result = super.listAs(Wrappers.getQueryWrapper(map)
				.select(CORE_ORG.DEFAULT_COLUMNS)
				.from(CORE_ORG).as("t")
				.orderBy(CORE_ORG.SORT.asc()), OrgVO.class);

		// 获取查询结果的 ID 列表
		List<Long> orgIds = result.stream()
				.map(OrgVO::getId)
				.collect(Collectors.toList());

		// 批量查询哪些 ID 有子节点
		if (Fc.isNotEmpty(orgIds)) {
			List<Long> hasChildrenIds = super.objListAs(
					Wrappers.getQueryWrapper(MapUtil.removeIf(map, entry -> entry.getKey().startsWith("parentId")))
							.select(QueryMethods.distinct(CORE_ORG.PARENT_ID))
							.from(CORE_ORG)
							.where(CORE_ORG.PARENT_ID.in(orgIds)),
					Long.class);

			// 设置 hasChildren 字段
			result.forEach(org ->
				org.setHasChildren(hasChildrenIds.contains(org.getId()))
			);
		}

		return result;
	}

	public List<Tree<Long>> tree(Map<String, Object> map) {
		return super.tree(Wrappers.getTreeWrapper(CoreOrg::getId, CoreOrg::getParentId)
				.map(map)
				.select(CoreOrg::getName)
				.orderBy(CoreOrg::getSort).asc());
	}

	public List<Tree<Long>> tree(Long parentId, Map<String, Object> map) {
		return super.tree(Wrappers.getTreeWrapper(CoreOrg::getId, CoreOrg::getParentId)
				.lazy(parentId)
				.map(map)
				.select(CoreOrg::getName)
				.orderBy(CoreOrg::getSort).asc());
	}

	public List<Long> queryChildIdById(Long id) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreOrg::getId)
				.like(CoreOrg::getAncestorId,id), Long.class);
	}
}
