package top.jpower.system.dbs.dao.org;


import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.query.QueryMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.dbs.dao.org.mapper.CoreOrgMapper;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.vo.OrgVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mybatisflex.core.query.QueryMethods.selectOne;
import static top.jpower.system.dbs.entity.org.table.CoreOrgTableDef.CORE_ORG;

/**
 * 组织机构数据访问对象
 * 
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class CoreOrgDao extends JpowerServiceImpl<CoreOrgMapper, CoreOrg> {

	private final IDialect dialect;

	/**
	 * 懒加载列表
	 *
	 * @param map
	 * @return
	 */
	public List<OrgVO> listLazyByParent(Map<String, Object> map) {
		return super.listAs(Wrappers.getQueryWrapper(map)
				.select(CORE_ORG.DEFAULT_COLUMNS)
				.select(QueryMethods.column(QueryMethods.exists(Wrappers.getQueryWrapper(map).select(selectOne().toSQL()).from(CORE_ORG).where(CORE_ORG.PARENT_ID.eq(CORE_ORG.as("t").ID))).toSql(Collections.singletonList(CORE_ORG), dialect)).as(OrgVO::getHasChildren))
				.as("t")
				.orderBy(CORE_ORG.SORT.asc()), OrgVO.class);
	}

	/**
	 * 获取顶级列表
	 *
	 * @param map
	 * @return
	 */
	public Pg<OrgVO> pageTop(Map<String, Object> map) {
		return super.pgAs(Wrappers.getQueryWrapper(map)
				.select(CORE_ORG.DEFAULT_COLUMNS)
				.select(QueryMethods.column(QueryMethods.exists(Wrappers.getQueryWrapper(map).select(selectOne().toSQL()).from(CORE_ORG).where(CORE_ORG.PARENT_ID.eq(CORE_ORG.as("t").ID))).toSql(Collections.singletonList(CORE_ORG), dialect)).as(OrgVO::getHasChildren))
				.as("t")
				.orderBy(CORE_ORG.SORT.asc()), OrgVO.class);
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
