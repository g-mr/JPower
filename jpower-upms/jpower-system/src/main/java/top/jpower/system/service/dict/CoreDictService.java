package top.jpower.system.service.dict;


import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.system.api.dto.SelectDTO;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.vo.DictVo;

import java.util.List;
import java.util.Map;

/**
 * 字典服务接口
 * 
 * @author mr.g
 */
public interface CoreDictService extends BaseService<CoreDict> {

    /**
     * 保存或者修改字典
     * 
     * @author mr.g
     * @param dict 字典实体
     * @return 是否保存成功
     */
    Long saveDict(CoreDict dict);

    /**
     * 查询字典列表
     * 
     * @author mr.g
     * @param map 查询条件
     * @return 字典视图列表
     */
	List<DictVo> listByType(Map<String, Object> map);

	/**
	 * 查询字典列表
	 *
	 * @author mr.g
	 * @param map 查询条件
	 * @return 字典视图分页列表
	 */
	Pg<DictVo> pageByType(Map<String, Object> map);

    /**
     * 通过字典类型查询字典列表
     * 
     * @author mr.g
     * @param dictTypeCode 字典类型编码
     * @return 字典列表
     */
    List<SelectDTO> listByTypeCode(String dictTypeCode);

	/**
	 * 停用字典
	 *
	 * @author mr.g
	 * @param id 字典id
	 * @return 是否停用成功
	 */
	boolean stopDict(Long id);

	/**
	 * 批量删除字典
	 *
	 * @author mr.g
	 * @param ids 字典id列表
	 * @return 是否删除成功
	 */
	boolean removeByIds(List<Long> ids);

	List<Tree<Long>> tree(String dictTypeCode);
}
