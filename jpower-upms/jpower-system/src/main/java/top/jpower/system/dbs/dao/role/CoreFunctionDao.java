package top.jpower.system.dbs.dao.role;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.util.LambdaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.role.mapper.CoreFunctionMapper;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.dbs.entity.function.CoreFunctionMenu;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;
import top.jpower.system.vo.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.system.dbs.entity.function.table.CoreFunctionTableDef.CORE_FUNCTION;
import static top.jpower.system.dbs.entity.role.table.CoreRoleFunctionTableDef.CORE_ROLE_FUNCTION;

/**
 * 功能数据访问对象
 * 
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class CoreFunctionDao extends JpowerServiceImpl<CoreFunctionMapper, CoreFunction> {

	private final IDialect dialect;

	/**
     * 获取功能树
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @param clientId 客户端ID
     * @return 功能树
     **/
    public List<Tree<Long>> treeMenuTypeByClientId(List<Long> roleIds, Long clientId) {
        return super.tree(Wrappers.getTreeWrapper(CoreFunction::getId, CoreFunction::getParentId)
								.select(CoreFunction::getFunctionName,CoreFunction::getFunctionType)
								.in(CoreFunction::getFunctionType, ListUtil.of(FunctionTypeEnum.MENU.getValue(), FunctionTypeEnum.BTN.getValue()))
								.eq(CoreFunction::getClientId,clientId)
								.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
								.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
                        		.orderBy(CoreFunction::getSort).asc());
    }

	/**
     * 获取功能树
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @param clientId 客户端ID
     * @return 功能树
     **/
	public List<Tree<Long>> treeFunction(List<Long> roleIds, Long clientId) {
		return super.tree(Wrappers.getTreeWrapper(CoreFunction::getId, CoreFunction::getParentId)
				.eq(CoreFunction::getClientId, clientId)
				.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
				.orderBy(CoreFunction::getSort).asc());
    }

	/**
	 * 获取功能URL
	 *
	 * @author mr.g
	 * @param roleIds 角色ID
	 * @param clientId 客户端ID
	 * @return 功能URL
	 **/
	public List<String> listUrlByRole(List<Long> roleIds, Long clientId) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getUrl)
				.eq(CoreFunction::getClientId, clientId)
				.isNotNull(CoreFunction::getUrl)
				.leftJoin(CoreRoleFunction.class).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.in(CoreRoleFunction::getRoleId, roleIds), String.class);
	}

	/**
	 * 获取功能树
	 *
	 * @author mr.g
	 * @param roleIds 角色ID
	 * @param clientId 客户端ID
	 * @return 功能树
	 **/
	public List<Tree<Long>> treeMenu(List<Long> roleIds, Long clientId) {
		return super.tree(Wrappers.getTreeWrapper(CoreFunction::getId, CoreFunction::getParentId)
				.select(CoreFunction::getFunctionName,CoreFunction::getCode,CoreFunction::getUrl,CoreFunction::getSort, CoreFunction::getClientId)
				.eq(CoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
				.eq(CoreFunction::getClientId, clientId)
				.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
				.orderBy(CoreFunction::getSort).asc());
	}

    /**
     * 获取功能的CODE和ID
     *
     * @author mr.g
     * @param codes CODE
     * @return code,id
     **/
    public Map<String, CoreFunction> selectIdByCode(Set<String> codes) {
        List<CoreFunction> functions = super.list(Wrappers.getQueryWrapper()
                .select(CoreFunction::getId,CoreFunction::getCode,CoreFunction::getAncestorId)
                .in(CoreFunction::getCode, codes));
        return functions.stream().collect(Collectors.toMap(CoreFunction::getCode, f->f));
    }

	/**
     * 接口列表
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @param clientId 客户端ID
     * @return 功能树
     **/
    public List<FunctionSimpleVO> listInterface(List<Long> roleIds, Long clientId) {
        return super.listAs(Wrappers.getQueryWrapper()
                        .select(CoreFunction::getId,CoreFunction::getParentId,CoreFunction::getCode,CoreFunction::getFunctionName,CoreFunction::getAlias,CoreFunction::getUrl,CoreFunction::getFunctionType)
                        .eq(CoreFunction::getFunctionType, FunctionTypeEnum.INTERFACE.getValue())
                        .eq(CoreFunction::getClientId,clientId)
						.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
						.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
						.orderBy(CoreFunction::getSort).asc(), FunctionSimpleVO.class);
    }

	/**
	 * 获取按钮CODE
	 *
	 * @author mr.g
	 * @param roleIds 角色ID
	 * @param clientId 客户端ID
	 * @return 功能CODE
	 **/
	public List<String> listCodeByRoleIdClientBtn(List<Long> roleIds, Long clientId) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getCode)
				.eq(CoreFunction::getClientId, clientId)
				.eq(CoreFunction::getFunctionType, FunctionTypeEnum.BTN.getValue())
				.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot()), String.class);
	}

	/**
	 * 获取顶级功能ID
	 *
	 * @author mr.g
	 * @return 顶级功能ID
	 **/
    public List<Long> queryNoMenuIdByTop() {
        List<Long> functionIds = super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getId)
                .eq(CoreFunction::getParentId, Fc.toLong(JpowerConstants.TOP_CODE))
                .ne(CoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue()), Long.class);

        functionIds.addAll(super.objListAs(Wrappers.getQueryWrapper()
                .select(CoreFunction::getId)
                .where(LambdaUtil.getFieldName(CoreFunction::getAncestorId) + " REGEXP ?", StringUtil.concat(StringPool.LEFT_BRACKET, StringUtil.join(functionIds, StringPool.SPILT), StringPool.RIGHT_BRACKET)), Long.class));
        return functionIds;
    }

	/**
	 * 根据功能CODE获取功能ID以及子孙功能ID
	 *
	 * @author mr.g
	 * @param functionCodes 功能CODE
	 * @return 功能ID
	 **/
	public List<Long> getAllIdByCode(Set<String> functionCodes) {
		List<Long> ids = super.objListAs(Wrappers.getQueryWrapper().select(CoreFunction::getId).in(CoreFunction::getCode, functionCodes), Long.class);

		if (Fc.isNotEmpty(ids)) {
			ids.forEach(id->{
				List<Long> descendants = super.objListAs(Wrappers.getQueryWrapper()
						.select(CoreFunction::getId)
						.ne(CoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
						.like(CoreFunction::getAncestorId, id), Long.class);

				ids.addAll(descendants);
			});
		}

		return ids;
	}

	/**
	 * 根据父ID获取接口的功能ID
	 *
	 * @author mr.g
	 * @param functionIds 功能ID
	 * @return 接口功能ID
	 **/
	public List<Long> getIdByParentIdInterface(List<Long> functionIds) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getId)
				.eq(CoreFunction::getFunctionType, FunctionTypeEnum.INTERFACE.getValue())
				.in(CoreFunction::getParentId, functionIds), Long.class);
	}

	/**
	 * 根据客户端ID获取功能ID和名称
	 *
	 * @author mr.g
	 * @param clientId 客户端ID
	 * @return 功能ID和名称
	 **/
	public List<SelectIdNameVO> selectByClientId(Long clientId) {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getId, CoreFunction::getFunctionName)
				.eq(CoreFunction::getParentId, Fc.toLong(TOP_CODE))
				.eq(CoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
				.eq(CoreFunction::getClientId, clientId), SelectIdNameVO.class);
	}

	/**
	 * 获取数据权限功能
	 *
	 * @author mr.g
	 * @param menuId 菜单ID
	 * @param map    查询条件
	 * @param roleIds 角色ID
	 * @return 数据权限功能
	 **/
	public List<DataFunctionVO> listDataFunction(Long menuId, Map<String, Object> map, List<Long> roleIds) {
		return super.listAs(Wrappers.getQueryWrapper(map)
						.as("t")
						.select(CORE_ROLE_FUNCTION.DEFAULT_COLUMNS)
						.select(QueryMethods.column(QueryMethods.exists(QueryMethods.selectOne()
								.where(CORE_FUNCTION.PARENT_ID.eq(CORE_FUNCTION.as("t").ID).and(CORE_FUNCTION.FUNCTION_TYPE.eq(FunctionTypeEnum.MENU.getValue())))).toSql(Collections.singletonList(CORE_FUNCTION), dialect)).as(DataFunctionVO::getHasChildren))
						.select(QueryMethods.column(QueryMethods.exists(QueryMethods.selectOne()
								.where(CORE_FUNCTION.PARENT_ID.eq(CORE_FUNCTION.as("t").ID).and(CORE_FUNCTION.FUNCTION_TYPE.ne(FunctionTypeEnum.MENU.getValue())))).toSql(Collections.singletonList(CORE_FUNCTION), dialect)).as(DataFunctionVO::getIsData))
						.leftJoin(CoreRoleFunction.class, Fc.isNotEmpty(roleIds)).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
						.in(CoreRoleFunction::getRoleId, roleIds, Fc.isNotEmpty(roleIds))
						.leftJoin(CoreFunctionMenu.class, Fc.notNull(menuId)).on(CoreFunctionMenu::getFunctionId, CoreFunction::getId)
						.eq(CoreFunctionMenu::getMenuId, menuId, Fc.notNull(menuId))
						.orderBy(CoreFunction::getSort).asc(), DataFunctionVO.class);
	}

	/**
	 * 根据角色ID、父ID、客户端ID获取功能ID
	 *
	 * @author mr.g
	 * @param roleIds 角色ID
	 * @param parentId 父ID
	 * @param clientId 客户端ID
	 * @return 功能ID
	 **/
	public List<Long> listIdByRoleIdParentId(List<Long> roleIds, Long parentId, Long clientId) {
		return super.objListAs(Wrappers.getQueryWrapper()
						.select(CoreFunction::getId)
						.eq(CoreFunction::getClientId,clientId)
						.eq(CoreFunction::getFunctionType, FunctionTypeEnum.BTN.getValue())
						.eq(CoreFunction::getParentId,parentId)
						.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
						.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
				, Long.class);
	}

	/**
	 * 获取功能树
	 *
	 * @author mr.g
	 * @param roleIds 角色ID
	 * @param parentId 父ID
	 * @param clientId 客户端ID
	 * @param topBtnIds 顶级按钮ID列表
	 * @return 功能树
	 **/
	public List<Tree<Long>> treeInfo(List<Long> roleIds, Long parentId, Long clientId, List<Long> topBtnIds, FunctionTypeEnum functionType, boolean isHide) {
		return super.tree(Wrappers.getTreeWrapper(CoreFunction::getId, CoreFunction::getParentId)
				.select(CoreFunction::getFunctionName,
						CoreFunction::getCode,
						CoreFunction::getRoute,
						CoreFunction::getUrl,
						CoreFunction::getIcon,
						CoreFunction::getSort,
						CoreFunction::getIsHide,
						CoreFunction::getTarget)
				.eq(CoreFunction::getFunctionType, functionType.getValue())
				.eq(CoreFunction::getClientId, clientId)
				.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
				.and(q->{
					if (Fc.equalsValue(parentId, TOP_CODE)){
						QueryCondition condition = CORE_FUNCTION.PARENT_ID.eq(parentId);
						for (Long pId: topBtnIds){
							condition.or(CORE_FUNCTION.ANCESTOR_ID.like(pId));
						}
						q.and(condition);
					} else {
						q.like(CoreFunction::getAncestorId, parentId);
					}
				}, Fc.notNull(parentId))
				.orderBy(CoreFunction::getSort).asc());
	}

	/**
	 * 获取功能列表
	 *
	 * @author mr.g
	 * @param map 查询条件
	 * @return 功能列表
	 **/
	public List<FunctionVO> listFunction(Map<String, Object> map) {
		Integer functionType = MapUtil.getInt(map, "functionType_eq");
		Long menuId = MapUtil.getLongRemoveKey(map, "menuId_eq");

		return super.listAs(Wrappers.getQueryWrapper(map)
					.from(CoreFunction.class).as("t")
					.select(CORE_FUNCTION.DEFAULT_COLUMNS)
					.select(QueryMethods.column(QueryMethods.exists(QueryMethods.selectOne()
							.from(CoreFunction.class)
							.where(CORE_FUNCTION.PARENT_ID.eq(CORE_FUNCTION.as("t").ID).and(CORE_FUNCTION.FUNCTION_TYPE.eq(functionType)))).toSql(Collections.singletonList(CORE_FUNCTION), dialect))
							.as(FunctionVO::getHasChildren))
					.leftJoin(CoreFunctionMenu.class, Fc.notNull(menuId)).on(CoreFunctionMenu::getFunctionId, CoreFunction::getId)
					.eq(CoreFunctionMenu::getMenuId, menuId)
					.orderBy(CoreFunction::getSort).asc(), FunctionVO.class);
	}

	/**
	 * 根据功能ID获取功能祖级ID
	 *
	 * @author mr.g
	 * @param id 功能ID
	 * @return 祖级ID
	 **/
	public String selectAncestorIdById(Long id) {
		return super.getObjAs(Wrappers.getQueryWrapper().select(CoreFunction::getAncestorId).eq(CoreFunction::getId, id), String.class);
	}

	/**
	 * 根据功能ID获取功能所有子功能
	 *
	 * @author mr.g
	 * @param ids 功能ID
	 * @return 功能列表
	 **/
	public List<CoreFunction> listDescendantsByIds(List<Long> ids) {
		return super.list(Wrappers.getQueryWrapper().in(CoreFunction::getId,ids).or(or->{
			for (Long id : ids) {
				or.or(CORE_FUNCTION.ANCESTOR_ID.like(id));
			}
		}));
	}

	/**
	 * 懒加载功能树
	 *
	 * @author mr.g
	 * @param parentId 父ID
	 * @param roleIds 角色ID
	 * @return 功能树
	 **/
	public List<Tree<Long>> lazyTreeByRoleIds(Long parentId, List<Long> roleIds) {
		return super.tree(Wrappers.getTreeWrapper(CoreFunction::getId, CoreFunction::getParentId)
					.lazy(parentId)
					.select(CoreFunction::getFunctionName,CoreFunction::getUrl, CoreFunction::getSort)
					.leftJoin(CoreRoleFunction.class, !ShieldUtil.isRoot()).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
					.in(CoreRoleFunction::getRoleId, roleIds, !ShieldUtil.isRoot())
					.orderBy(CoreFunction::getSort).asc());
	}

	/**
	 * 获取功能菜单
	 *
	 * @author mr.g
	 * @param functionTypeEnum 功能类型
	 * @return 功能菜单
	 **/
	public List<CoreFunction> listMenu(FunctionTypeEnum functionTypeEnum) {
		return super.list(Wrappers.getQueryWrapper().eq(CoreFunction::getFunctionType, functionTypeEnum.getValue()));
	}

	public Long getIdByCode(String code) {
		return super.getObjAs(Wrappers.getQueryWrapper()
				.select(CoreFunction::getId)
				.eq(CoreFunction::getCode,code) , Long.class);
	}

	public boolean existsByCode(CodeExistsBO codeExistsBO) {
		return super.exists(Wrappers.getQueryWrapper()
				.eq(CoreFunction::getCode, codeExistsBO.getCode())
				.ne(CoreFunction::getId, codeExistsBO.getId(), Fc.notNull(codeExistsBO.getId())));
	}
}


