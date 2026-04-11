package top.jpower.system.dbs.dao.dict;

import cn.hutool.core.lang.tree.Tree;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictTypeMapper;
import top.jpower.system.dbs.entity.dict.CoreDictType;

import java.util.List;

/**
 * 字典类型数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreDictTypeDao extends JpowerServiceImpl<CoreDictTypeMapper, CoreDictType> {


	public List<Tree<Long>> tree() {
		return super.tree(Wrappers.getTreeWrapper(CoreDictType::getId, CoreDictType::getParentId)
				.select(CoreDictType::getDictTypeName,
						CoreDictType::getDictTypeCode,
						CoreDictType::getDelEnabled,
						CoreDictType::getIsTree)
				.orderBy(CoreDictType::getSortNum).asc());
	}

	/**
	 * 根据id查询字典类型编码
	 * @param ids 主键
	 * @return 字典类型编码
	 */
	public List<String> listCodeByIds(List<Long> ids) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreDictType::getDictTypeCode)
				.in(CoreDictType::getId,ids), String.class);
	}

	public String getCodeById(Long id) {
		return super.getObjAs(Wrappers.getQueryWrapper()
				.select(CoreDictType::getDictTypeCode)
				.eq(CoreDictType::getId,id), String.class);
	}

	/**
	 * 根据id查询字典类型编码
	 * @param ids 主键
	 * @return 是否存在
	 */
	public boolean existsByQuery(List<Long> ids) {
		return super.exists(Wrappers.getQueryWrapper().in(CoreDictType::getParentId, ids));
	}

	/**
	 * 根据id删除字典类型
	 * @param ids 主键
	 * @return 是否删除成功
	 */
	public boolean removeByIdsDel(List<Long> ids) {
		return super.removeReal(Wrappers.getQueryWrapper()
				.in(CoreDictType::getId, ids)
				.eq(CoreDictType::getDelEnabled, YN01Enum.Y.getValue()));
	}
}
