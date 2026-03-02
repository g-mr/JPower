package top.jpower.system.dbs.dao.role;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.util.LambdaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.system.dbs.dao.role.mapper.CoreFunctionMapper;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.dbs.entity.function.CoreFunctionMenu;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;
import top.jpower.system.vo.DataFunctionVo;
import top.jpower.system.vo.SelectIdNameVO;

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

    private static final String ROLE_SQL = "select function_id from tb_core_role_function where role_id in ({})";

    public List<Tree<String>> treeMenuTypeByClientId(List<Long> roleIds, Long clientId) {
        return super.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                        .select(TbCoreFunction::getFunctionName,TbCoreFunction::getFunctionType,TbCoreFunction::getSort)
                        .in(TbCoreFunction::getFunctionType, ListUtil.of(FunctionTypeEnum.MENU.getValue(),FunctionTypeEnum.BTN.getValue()))
                        // 如果不是超级用户，则查出自己权限的菜单
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId, StringUtil.format(ROLE_SQL, StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .eq(TbCoreFunction::getClientId,clientId)
                        .orderByAsc(TbCoreFunction::getSort));
    }

    /**
     * 获取功能的CODE和ID
     *
     * @author mr.g
     * @param codes CODE
     * @return code,id
     **/
    public Map<String, TbCoreFunction> selectIdByCode(Set<String> codes) {
        List<TbCoreFunction> functions = super.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getId,TbCoreFunction::getCode,TbCoreFunction::getAncestorId)
                .in(TbCoreFunction::getCode, codes));
        return functions.stream().collect(Collectors.toMap(TbCoreFunction::getCode, f->f));
    }

    public List<Map<String, Object>> listInterface(List<Long> roleIds, Long clientId) {
        List<Map<String, Object>> list = super.listMaps(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getCode,TbCoreFunction::getFunctionName,TbCoreFunction::getAlias,TbCoreFunction::getUrl,TbCoreFunction::getFunctionType)
                        .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.INTERFACE.getValue())
                        .eq(TbCoreFunction::getClientId,clientId)
                        .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId, StringUtil.format("select function_id from tb_core_role_function where role_id in ({})",StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE))));

        return list.stream().map(map-> MapUtil.edit(map, mp -> new Map.Entry<String, Object>() {
            @Override
            public String getKey() {
                return StringUtil.underlineToHump(mp.getKey());
            }

            @Override
            public Object getValue() {
                return mp.getValue();
            }

            @Override
            public Object setValue(Object value) {
                return mp.setValue(value);
            }

        })).collect(Collectors.toList());
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
	public List<DataFunctionVo> listDataFunction(Long menuId, Map<String, Object> map, List<Long> roleIds) {
		return super.listAs(Wrappers.getQueryWrapper(map)
						.as("t")
						.select(CORE_ROLE_FUNCTION.DEFAULT_COLUMNS)
						.select(QueryMethods.column(QueryMethods.exists(QueryMethods.selectOne()
								.where(CORE_FUNCTION.PARENT_ID.eq(CORE_FUNCTION.as("t").ID).and(CORE_FUNCTION.FUNCTION_TYPE.eq(FunctionTypeEnum.MENU.getValue())))).toSql(Collections.singletonList(CORE_FUNCTION), dialect)).as(DataFunctionVo::getHasChildren))
						.select(QueryMethods.column(QueryMethods.exists(QueryMethods.selectOne()
								.where(CORE_FUNCTION.PARENT_ID.eq(CORE_FUNCTION.as("t").ID).and(CORE_FUNCTION.FUNCTION_TYPE.ne(FunctionTypeEnum.MENU.getValue())))).toSql(Collections.singletonList(CORE_FUNCTION), dialect)).as(DataFunctionVo::getIsData))
						.leftJoin(CoreRoleFunction.class, Fc.isNotEmpty(roleIds)).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
						.in(CoreRoleFunction::getRoleId, roleIds, Fc.isNotEmpty(roleIds))
						.leftJoin(CoreFunctionMenu.class, Fc.notNull(menuId)).on(CoreFunctionMenu::getFunctionId, CoreFunction::getId)
						.eq(CoreFunctionMenu::getMenuId, menuId, Fc.notNull(menuId))
						.orderBy(CoreFunction::getSort).asc(), DataFunctionVo.class);
	}
}


