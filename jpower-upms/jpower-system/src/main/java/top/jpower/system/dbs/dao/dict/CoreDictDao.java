package top.jpower.system.dbs.dao.dict;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.config.datasource.DefaultDataSourceProcessor;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.api.dto.SelectDTO;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.vo.DictVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mybatisflex.core.query.QueryMethods.selectOne;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.system.dbs.entity.dict.table.CoreDictTableDef.CORE_DICT;

/**
 * 字典数据访问对象
 * 
 * @author mr.g
 */
@Repository
@RequiredArgsConstructor
public class CoreDictDao extends JpowerServiceImpl<CoreDictMapper, CoreDict> {

	private final IDialect dialect;

	/**
	 * 修改字典类型编码
	 *
	 * @param dictTypeCode 字典类型编码
	 * @param typeCode 字典类型编码
	 * @return 修改结果
	 */
	public boolean updateDictTypeCode(String dictTypeCode, String typeCode) {
		return super.update(UpdateEntity.of(CoreDict.class).setDictTypeCode(dictTypeCode),
				Wrappers.getQueryWrapper().eq(CoreDict::getDictTypeCode, typeCode));
	}

	/**
	 * 根据字典类型编码删除字典
	 *
	 * @param listCode 字典类型编码
	 * @return 删除结果
	 */
	public boolean removeByTypeCode(List<String> listCode) {
		return super.removeReal(Wrappers.getQueryWrapper()
				.in(CoreDict::getDictTypeCode, listCode));
	}

	private QueryWrapper getQueryWrapper(Map<String, Object> map) {
		return Wrappers.getQueryWrapper(map, "t")
				.select(CORE_DICT.DEFAULT_COLUMNS)
				.select(CORE_DICT.as("p").NAME.as(DictVO::getParentName))
				.select(QueryMethods.column(QueryMethods.exists(selectOne().from(CORE_DICT).where(CORE_DICT.PARENT_ID.eq(CORE_DICT.as("t").ID))).toSql(Collections.singletonList(CORE_DICT), dialect)).as(DictVO::getHasChildren))
				.from(CORE_DICT.as("t"))
				.leftJoin(CORE_DICT.as("p")).on(CORE_DICT.as("p").ID.eq(CORE_DICT.as("t").PARENT_ID))
				.eq(CoreDict::getTenantCode, ShieldUtil.getTenantCode(), ShieldUtil.isRoot())
				.orderBy(CoreDict::getSortNum).asc();
	};

	public List<DictVO> listByType(Map<String, Object> map) {
		return super.listAs(getQueryWrapper(map), DictVO.class);
	}

	public Pg<DictVO> pageByType(Map<String, Object> map) {
		return super.pgAs(getQueryWrapper(map), DictVO.class);
	}

	public CoreDict getByDictTypeCode(String dictTypeCode, String code) {
		return super.getOne(Wrappers.getQueryWrapper()
				.eq(CoreDict::getDictTypeCode, dictTypeCode)
				.eq(CoreDict::getCode, code)
				.eq(CoreDict::getTenantCode,DEFAULT_TENANT_CODE, ShieldUtil.isRoot()));
	}

	/**
	 * 根据字典ID判断字典是否存在
	 *
	 * @param id 字典ID
	 * @return 字典是否存在
	 */
	public boolean existsByParentIdNoStop(Long id) {
		return super.exists(Wrappers.getQueryWrapper().eq(CoreDict::getIsStop, YN01Enum.N.getValue()).eq(CoreDict::getParentId, id));
	}

	/**
	 * 停用字典
	 *
	 * @param id 字典ID
	 * @return 停用结果
	 */
	public boolean stop(Long id, Boolean status) {
		return super.updateById(UpdateEntity.of(CoreDict.class).setIsStop(status).setId(id));
	}

	/**
	 * 根据字典ID判断下级字典是否存在
	 *
	 * @param ids 字典ID
	 * @return 字典是否存在
	 */
	public boolean existsByParentIds(List<Long> ids) {
		return super.exists(Wrappers.getQueryWrapper()
				.in(CoreDict::getParentId, ids));
	}

	/**
	 * 根据字典类型编码获取字典树
	 *
	 * @param dictTypeCode 字典类型编码
	 * @return 字典树
	 */
	public List<Tree<Long>> treeByType(String dictTypeCode) {
		return super.tree(Wrappers.getTreeWrapper(CoreDict::getId, CoreDict::getParentId).eq(CoreDict::getDictTypeCode, dictTypeCode));
	}

    /**
     * 获取字典下拉列表
     * <br />
     *
     * @param dictTypeCode 字典类型编码
     * @param requestLocale 请求语言
     * @see DefaultDataSourceProcessor
     * @return 字典下拉列表
     */
    @UseDataSource("@master@")
    public List<SelectDTO> listSelect(String dictTypeCode, String requestLocale) {
        return super.listAs(Wrappers.getQueryWrapper()
                .select(CoreDict::getCode, CoreDict::getName)
                .eq(CoreDict::getDictTypeCode, dictTypeCode)
                .eq(CoreDict::getLocale, requestLocale)
                .eq(CoreDict::getTenantCode, DEFAULT_TENANT_CODE, ShieldUtil.isRoot()), SelectDTO.class);
    }

	/**
	 * 获取字典下拉列表
	 * <p>
	 * 根据字典值自动识别真实数据类型：
	 * 可转换为整数则返回Integer，可转换为布尔值则返回Boolean，否则保持String
	 * </p>
	 *
	 * @param dictTypeCode 字典类型编码
	 * @return 字典下拉列表
	 */
	public List<Tree<Long>> dictSelect(String dictTypeCode) {
		return super.tree(Wrappers.getTreeWrapper(CoreDict::getId, CoreDict::getParentId)
				.select(CORE_DICT.CODE.as("value"))
				.select(CORE_DICT.NAME.as("label"))
				.eq(CoreDict::getDictTypeCode, dictTypeCode)
				.eq(CoreDict::getIsStop, YN01Enum.N.getValue())
				.eq(CoreDict::getTenantCode, ShieldUtil.getTenantCode())
				.orderBy(CoreDict::getSortNum).asc());
	}


}
